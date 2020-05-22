package com.security.foxtc.config;


import com.security.foxtc.component.email.EmailAuthenticationFilter;
import com.security.foxtc.component.email.EmailAuthenticationProvider;
import com.security.foxtc.component.email.service.EmailUserDetailsService;
import com.security.foxtc.handler.BaseAuthenticationFailureHandler;
import com.security.foxtc.handler.BaseAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 邮箱配置类
 *
 * @author tangchen
 */
@Component
public class EmailSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Autowired
    private EmailUserDetailsService emailUserDetailsService;

    @Autowired
    private BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;
    @Autowired
    private BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;


    @Override
    public void configure(HttpSecurity http) throws Exception {


        EmailAuthenticationFilter authenticationFilter = new EmailAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(baseAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(baseAuthenticationFailureHandler);

        EmailAuthenticationProvider authenticationProvider = new EmailAuthenticationProvider();
        authenticationProvider.emailUserDetailsService(emailUserDetailsService);
        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
