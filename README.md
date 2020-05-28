##  Security  Extension
### 项目介绍
Security Extension是基于SpringCloud Security Oauth2，在其基础上新增了多种登录方式，扩展了用户登录的方式。主要包括邮箱验证码登录，手机验证码登录，第三方社交登录。支持登录失败自定义返回。实现了登出，从tokenStore中移除认证信息，从而实现统一登出。
### 项目结构
项目主要包括component config handler translator

#### Component 
主要是扩展登录的组件，包括email,mobile,social三个组件
##### Email登录组件


```
 1. EmailUserDetailsService
    包括两个方法,获取邮箱验证码，获取系统用户信息
 2. EmailAuthenticationFilter 
    邮箱登陆的过滤器，主要用来拦截端点/email/token的请求，封装EmailAuthenticationToken,然后交给AuthenticationManager处理进行验证
 3. EmailAuthenticationProvider 
    主要验证用户的邮箱验证码是否正确，构建认证成功的EmailAuthenticationToken
 4. EmailAuthenticationToken 
    主要提供两个构造方法，未认证的构造方法，认证成功的方法
```




 
##### Mobile登录组件



```
  1. MobileUserDetailsService
     包括两个方法,获取手机验证码，获取系统用户信息
  2. MobileAuthenticationFilter 
     手机登陆的过滤器，主要用来拦截端点/mobile/token的请求，封装MobileAuthenticationToken,然后交给AuthenticationManager处理进行验证
  3. MobileAuthenticationProvider 
     主要验证用户的手机验证码是否正确，构建认证成功的MobileAuthenticationToken
  4. MobileAuthenticationToken 
     主要提供两个构造方法，未认证的构造方法，认证成功的方法
```


##### Social登录组件

 
```
 1. SocialUserDetailsService
    包括两个方法,获取第三方社交用户信息，获取系统用户信息
 2. SocialAuthenticationFilter 
    第三方社交登陆的过滤器，主要用来拦截端点/social/token的请求，封装SocialAuthenticationToken,然后交给AuthenticationManager处理进行验证
 3. SocialAuthenticationProvider 
    主要验证用户的第三方社交验证码是否正确，构建认证成功的SocialAuthenticationToken
 4.  SocialAuthenticationToken 
     主要提供两个构造方法，未认证的构造方法，认证成功的方法
```


#### Config
主要是用于配置扩展登录组件

```
1. EmailSecurityConfigurerAdapter 邮箱登录的配置类
2. MobileSecurityConfigurerAdapter 手机登录的配置类
3. SocialSecurityConfigurerAdapter 第三方社交登录的配置类
```

#### handler
主要用来处理扩展登录，登录成功，失败的处理器，主要包括以下几个类。

```
1. BaseAuthenticationFailureHandler    扩展登录失败的处理器
2. BaseAuthenticationSuccessHandler    扩展登录成功的处理器
3. BaseLogoutSuccessHandler            成功登出的处理器
```


#### Translator
主要是用来处理登录失败的自定义参数，主要用于原生的密码登录失败处理
主要包括以下类 
```
1. BaseWebResponseExceptionTranslator
2. CustomOAuth2Exception 内部类，自定义OAuth2Exception
3. CustomOauthExceptionSerializer  内部类，自定义序列化类
```
#### 快速开始

1. 引入maven依赖

```
        <dependency>
            <groupId>com.github.tangchen-blip</groupId>
            <artifactId>security-extension</artifactId>
            <version>1.0.0</version>
        </dependency>
```


2. 在自定义中增加WebSecurityConfigurerAdapter

```
    @Autowired
    private EmailSecurityConfigurerAdapter emailSecurityConfigurerAdapter;
    @Autowired
    private MobileSecurityConfigurerAdapter mobileSecurityConfigurerAdapter;
    @Autowired
    private SocialSecurityConfigurerAdapter socialSecurityConfigurerAdapter;
    
     @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler())
                .permitAll()
                .and().authorizeRequests()
                .antMatchers("/email/token", "/mobile/token", "/social/token")//这里放开认证的端点
                .permitAll().and().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .disable()
                .csrf().disable();//自定义
                
        //这里引入扩展登陆的配置        
        http.apply(emailSecurityConfigurerAdapter)
                .and().apply(mobileSecurityConfigurerAdapter)
                .and().apply(socialSecurityConfigurerAdapter);
        
    }
```
3.默认登陆异常返回参数自定义

```
security.oauth2.login.fail.code 响应码参数名 默认为code

security.oauth2.login.fail.msg  响应信息参数名 默认为msg
```

#### Demo
[Demo下载地址](https://github.com/tangchen-blip/blip-auth-server)
