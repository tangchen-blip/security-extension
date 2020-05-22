package com.security.foxtc.config;


import com.security.foxtc.component.mobile.MobileAuthenticationFilter;
import com.security.foxtc.component.mobile.MobileAuthenticationProvider;
import com.security.foxtc.component.mobile.service.MobileUserDetailsService;
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
public class MobileSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private MobileUserDetailsService mobileUserDetailsService;

    @Autowired
    private BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;
    @Autowired
    private BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;


    @Override
    public void configure(HttpSecurity http) throws Exception {


        MobileAuthenticationFilter authenticationFilter = new MobileAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(baseAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(baseAuthenticationFailureHandler);

        MobileAuthenticationProvider authenticationProvider = new MobileAuthenticationProvider();
        authenticationProvider.mobileUserDetailsService(mobileUserDetailsService);
        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
