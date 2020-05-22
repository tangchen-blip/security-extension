package com.security.foxtc.component.email;

import com.security.foxtc.component.email.service.EmailUserDetailsService;
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
public class EmailAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserDetailsChecker preAuthenticationChecks = new EmailAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new EmailAuthenticationProvider.DefaultPostAuthenticationChecks();

    private EmailUserDetailsService emailUserDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof EmailAuthenticationToken)) {
            throw new InternalAuthenticationServiceException("Authentication can not cast of EmailAuthenticationToken");
        }
        EmailAuthenticationToken authenticationToken = (EmailAuthenticationToken) authentication;
        String email = authenticationToken.getName();
        String code = Objects.toString(authenticationToken.getCredentials(), "");
        String cacheCode = emailUserDetailsService.loadCodeByEmail(email);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(cacheCode)) {
            throw new InternalAuthenticationServiceException("验证码为空");
        }
        if (!code.equals(cacheCode)) {
            throw new InternalAuthenticationServiceException("邮箱或验证码错误");
        }
        UserDetails userDetails = emailUserDetailsService.loadUserByEmail(email);
        this.preAuthenticationChecks.check(userDetails);
        this.postAuthenticationChecks.check(userDetails);
        EmailAuthenticationToken emailAuthenticationToken = new EmailAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        emailAuthenticationToken.setDetails(authenticationToken.getDetails());
        return emailAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 设置验证码的Service
     *
     * @param emailUserDetailsService
     * @return
     */
    public EmailAuthenticationProvider emailUserDetailsService(EmailUserDetailsService emailUserDetailsService) {
        this.emailUserDetailsService = emailUserDetailsService;
        return this;
    }


    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User account credentials have expired");
                throw new CredentialsExpiredException(EmailAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
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
                throw new LockedException(EmailAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if (!user.isEnabled()) {
                log.debug("User account is disabled");
                throw new DisabledException(EmailAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if (!user.isAccountNonExpired()) {
                log.debug("User account is expired");
                throw new AccountExpiredException(EmailAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
