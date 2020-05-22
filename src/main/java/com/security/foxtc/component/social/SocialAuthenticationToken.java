package com.security.foxtc.component.social;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 社交账号authToken
 *
 * @author tangchen
 */
public class SocialAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 账号，用户信息
     */
    private Object principal;

    /**
     * 用户凭证
     */
    private Object credentials;

    /**
     * 认证成功后的构造函数
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public SocialAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    /**
     * 未认证前的构造函数
     *
     * @param principal
     * @param credentials
     */
    public SocialAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            return;
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
