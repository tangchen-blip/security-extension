package com.security.foxtc.config;


import com.security.foxtc.component.social.SocialAuthenticationFilter;
import com.security.foxtc.component.social.SocialAuthenticationProvider;
import com.security.foxtc.component.social.service.SocialUserDetailsService;
import com.security.foxtc.handler.BaseAuthenticationFailureHandler;
import com.security.foxtc.handler.BaseAuthenticationSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 邮箱配置类
 *
 * @author tangchen
 */
public class SocialSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    private SocialUserDetailsService socialUserDetailsService;


    private BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;

    private BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;


    public SocialSecurityConfigurerAdapter baseAuthenticationFailureHandler(BaseAuthenticationFailureHandler baseAuthenticationFailureHandler) {
        this.baseAuthenticationFailureHandler = baseAuthenticationFailureHandler;
        return this;
    }

    public SocialSecurityConfigurerAdapter baseAuthenticationSuccessHandler(BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler) {
        this.baseAuthenticationSuccessHandler = baseAuthenticationSuccessHandler;
        return this;
    }

    public SocialSecurityConfigurerAdapter socialUserDetailsService(SocialUserDetailsService socialUserDetailsService) {
        this.socialUserDetailsService = socialUserDetailsService;
        return this;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {


        SocialAuthenticationFilter authenticationFilter = new SocialAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(baseAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(baseAuthenticationFailureHandler);

        SocialAuthenticationProvider authenticationProvider = new SocialAuthenticationProvider();
        authenticationProvider.socialUserDetailsService(socialUserDetailsService);
        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
