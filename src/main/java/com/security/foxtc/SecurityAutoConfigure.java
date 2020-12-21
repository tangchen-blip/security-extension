package com.security.foxtc;


import com.security.foxtc.component.email.service.EmailUserDetailsService;
import com.security.foxtc.component.email.service.EmailUserDetailsServiceImpl;
import com.security.foxtc.component.mobile.service.MobileUserDetailsService;
import com.security.foxtc.component.mobile.service.MobileUserDetailsServiceImpl;
import com.security.foxtc.component.social.model.SocialUserDetailsServiceImpl;
import com.security.foxtc.component.social.service.SocialUserDetailsService;
import com.security.foxtc.config.EmailSecurityConfigurerAdapter;
import com.security.foxtc.config.MobileSecurityConfigurerAdapter;
import com.security.foxtc.config.SocialSecurityConfigurerAdapter;
import com.security.foxtc.handler.BaseAuthenticationFailureHandler;
import com.security.foxtc.handler.BaseAuthenticationSuccessHandler;
import com.security.foxtc.handler.BaseLogoutSuccessHandler;
import com.security.foxtc.translator.BaseWebResponseExceptionTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author tangchen
 */
@Configuration
@ConditionalOnClass(SecurityAutoConfigure.class)
@EnableConfigurationProperties(value = FailureProperties.class)
public class SecurityAutoConfigure {


    @Bean
    @ConditionalOnMissingBean(BaseAuthenticationFailureHandler.class)
    public BaseAuthenticationFailureHandler baseAuthenticationFailureHandler(FailureProperties failureProperties) {
        BaseAuthenticationFailureHandler baseAuthenticationFailureHandler = new BaseAuthenticationFailureHandler();
        baseAuthenticationFailureHandler.setCodeParam(failureProperties.getStatusParam());
        baseAuthenticationFailureHandler.setMsgParam(failureProperties.getMsgParam());
        return baseAuthenticationFailureHandler;
    }

    @Bean
    @ConditionalOnMissingBean(BaseAuthenticationSuccessHandler.class)
    public BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler(ClientDetailsService clientDetailsService,
                                                                             AuthorizationServerTokenServices authorizationServerTokenServices,
                                                                             PasswordEncoder passwordEncoder) {
        BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler = new BaseAuthenticationSuccessHandler()
                .clientDetailsService(clientDetailsService)
                .authorizationServerTokenServices(authorizationServerTokenServices)
                .passwordEncoder(passwordEncoder);
        return baseAuthenticationSuccessHandler;
    }

    @Bean
    @ConditionalOnMissingBean(BaseLogoutSuccessHandler.class)
    public BaseLogoutSuccessHandler baseLogoutSuccessHandler(TokenStore tokenStore) {
        return new BaseLogoutSuccessHandler().tokenStore(tokenStore);
    }

    @Bean
    @ConditionalOnMissingBean(BaseWebResponseExceptionTranslator.class)
    public BaseWebResponseExceptionTranslator baseWebResponseExceptionTranslator(FailureProperties failureProperties) {
        BaseWebResponseExceptionTranslator baseWebResponseExceptionTranslator = new BaseWebResponseExceptionTranslator();
        baseWebResponseExceptionTranslator.setCodeParam(failureProperties.getStatusParam());
        baseWebResponseExceptionTranslator.setMsgParam(failureProperties.getMsgParam());
        return baseWebResponseExceptionTranslator;
    }

    @Bean
    @ConditionalOnMissingBean(EmailSecurityConfigurerAdapter.class)
    EmailSecurityConfigurerAdapter emailSecurityConfigurerAdapter(EmailUserDetailsService emailUserDetailsService,
                                                                  BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                                                                  BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler) {
        return new EmailSecurityConfigurerAdapter()
                .baseAuthenticationFailureHandler(baseAuthenticationFailureHandler)
                .baseAuthenticationSuccessHandler(baseAuthenticationSuccessHandler)
                .emailUserDetailsService(emailUserDetailsService);
    }

    @Bean
    @ConditionalOnMissingBean(MobileSecurityConfigurerAdapter.class)
    MobileSecurityConfigurerAdapter mobileSecurityConfigurerAdapter(MobileUserDetailsService mobileUserDetailsService,
                                                                    BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                                                                    BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler) {
        return new MobileSecurityConfigurerAdapter()
                .baseAuthenticationFailureHandler(baseAuthenticationFailureHandler)
                .baseAuthenticationSuccessHandler(baseAuthenticationSuccessHandler)
                .mobileUserDetailsService(mobileUserDetailsService);
    }

    @Bean
    @ConditionalOnMissingBean(SocialSecurityConfigurerAdapter.class)
    SocialSecurityConfigurerAdapter socialSecurityConfigurerAdapter(SocialUserDetailsService socialUserDetailsService,
                                                                    BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                                                                    BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler) {
        return new SocialSecurityConfigurerAdapter()
                .baseAuthenticationFailureHandler(baseAuthenticationFailureHandler)
                .baseAuthenticationSuccessHandler(baseAuthenticationSuccessHandler)
                .socialUserDetailsService(socialUserDetailsService);
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(EmailUserDetailsService.class)
    EmailUserDetailsService emailUserDetailsService(){
        return new EmailUserDetailsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(SocialUserDetailsService.class)
    SocialUserDetailsService socialUserDetailsService(){
        return new SocialUserDetailsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(MobileUserDetailsServiceImpl.class)
    MobileUserDetailsServiceImpl mobileUserDetailsService(){
        return new MobileUserDetailsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }
}
