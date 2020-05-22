package com.security.foxtc.component.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * 邮箱认证token的封装类
 *
 * @author tangchen
 */
public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 用户信息
     */
    private final Object principal;
    /**
     * 验证码
     */
    private Object credentials;

    /**
     * 未认证的构造函数
     *
     * @param principal
     * @param credentials
     */
    EmailAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    /**
     * 认证成功的构造函数
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    EmailAuthenticationToken(Object principal,
                             Object credentials,
                             Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
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
