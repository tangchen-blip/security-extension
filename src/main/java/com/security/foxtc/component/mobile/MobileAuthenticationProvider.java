package com.security.foxtc.component.mobile;

import com.security.foxtc.component.mobile.service.MobileUserDetailsService;
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
 * 邮箱与验证码的比较
 *
 * @author tangchen
 */
@Slf4j
public class MobileAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserDetailsChecker preAuthenticationChecks = new MobileAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new MobileAuthenticationProvider.DefaultPostAuthenticationChecks();

    private MobileUserDetailsService mobileUserDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof MobileAuthenticationToken)) {
            throw new InternalAuthenticationServiceException("Authentication can not cast of EmailAuthenticationToken");
        }
        MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = authenticationToken.getName();

        String code = Objects.toString(authenticationToken.getCredentials(), "");
        String cacheCode = mobileUserDetailsService.loadCodeByMobile(mobile);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(cacheCode)) {
            throw new InternalAuthenticationServiceException("验证码为空");
        }
        if (!code.equals(cacheCode)) {
            throw new InternalAuthenticationServiceException("邮箱或验证码错误");
        }
        UserDetails userDetails = mobileUserDetailsService.loadUserByMobile(mobile);
        this.preAuthenticationChecks.check(userDetails);
        this.postAuthenticationChecks.check(userDetails);
        MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        mobileAuthenticationToken.setDetails(authenticationToken.getDetails());
        return mobileAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 设置验证码的Service
     *
     * @param mobileUserDetailsService
     * @return
     */
    public MobileAuthenticationProvider mobileUserDetailsService(MobileUserDetailsService mobileUserDetailsService) {
        this.mobileUserDetailsService = mobileUserDetailsService;
        return this;
    }


    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User account credentials have expired");
                throw new CredentialsExpiredException(MobileAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
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
                throw new LockedException(MobileAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if (!user.isEnabled()) {
                log.debug("User account is disabled");
                throw new DisabledException(MobileAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if (!user.isAccountNonExpired()) {
                log.debug("User account is expired");
                throw new AccountExpiredException(MobileAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
