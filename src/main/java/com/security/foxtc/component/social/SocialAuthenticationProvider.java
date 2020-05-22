package com.security.foxtc.component.social;


import com.security.foxtc.component.social.model.SocialUserDetails;
import com.security.foxtc.component.social.service.SocialUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.Objects;

/**
 * 登录验证
 *
 * @author tangchen
 */
@Slf4j
public class SocialAuthenticationProvider implements AuthenticationProvider {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserDetailsChecker preAuthenticationChecks = new SocialAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new SocialAuthenticationProvider.DefaultPostAuthenticationChecks();

    private SocialUserDetailsService socialUserDetailsService;

    /**
     * 社交唯一标识
     */
    private String socialIdParam = "unionid";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof SocialAuthenticationToken)) {
            throw new InternalAuthenticationServiceException("Authentication can not cast of SocialAuthenticationToken");
        }
        SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
        String socialToken = Objects.toString(authentication.getPrincipal(), null);
        if (StringUtils.isBlank(socialToken)) {
            throw new InternalAuthenticationServiceException("认证失败，第三方token为空");
        }
        SocialUserDetails socialInfo = socialUserDetailsService.loadUserBySocialToken(socialToken);
        if (Objects.isNull(socialInfo) || StringUtils.isBlank(socialInfo.getSocialId())) {
            throw new InternalAuthenticationServiceException("认证失败,获取社交信息失败，检查token");
        }
        UserDetails userDetails = socialUserDetailsService.loadUserBySocialId(socialInfo.getSocialId());
        if (Objects.isNull(userDetails) || StringUtils.isBlank(userDetails.getUsername())) {
            throw new InternalAuthenticationServiceException("社交账号没有绑定系统用户");
        }
        this.preAuthenticationChecks.check(userDetails);
        this.postAuthenticationChecks.check(userDetails);
        SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        socialAuthenticationToken.setDetails(authentication.getDetails());
        return socialAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SocialAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public SocialAuthenticationProvider socialUserDetailsService(SocialUserDetailsService socialUserDetailsService) {
        this.socialUserDetailsService = socialUserDetailsService;
        return this;
    }

    public void setSocialIdParam(String socialIdParam) {
        this.socialIdParam = socialIdParam;
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User account credentials have expired");
                throw new CredentialsExpiredException(SocialAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
            }
        }
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        private DefaultPreAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("User account is locked");
                throw new LockedException(SocialAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if (!user.isEnabled()) {
                log.debug("User account is disabled");
                throw new DisabledException(SocialAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if (!user.isAccountNonExpired()) {
                log.debug("User account is expired");
                throw new AccountExpiredException(SocialAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
