package com.security.foxtc.component.email;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 定义拦截器，拦截相应的请求封装相应的登录参数
 *
 * @author tangchen
 */
public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    /**
     * 是否只支持post
     */
    private boolean postOnly = true;

    private final static String REQUEST_POST = "POST";
    /**
     * 默认拦截的地址
     */
    private final static String DEFAULT_TOKEN_URI = "/email/token";
    /**
     * 默认拦截方法
     */
    private final static String DEFAULT_TOKEN_METHOD = "POST";

    /**
     * 邮箱参数
     */
    private String emailNameParam = "email";
    /**
     * 验证码参数
     */
    private String emailCodeParam = "code";

    public EmailAuthenticationFilter() {
        super(new AntPathRequestMatcher(DEFAULT_TOKEN_URI, DEFAULT_TOKEN_METHOD));
    }

    public EmailAuthenticationFilter(String tokenUri, String tokenMethod) {
        super(new AntPathRequestMatcher(tokenUri, tokenMethod));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !REQUEST_POST.equals(httpServletRequest.getMethod())) {
            throw new AuthenticationServiceException("请求方法只能为POST");
        }
        String email = obtainEmail(httpServletRequest);
        if (StringUtils.isEmpty(email)) {
            throw new AuthenticationServiceException("邮箱为空");
        }
        String code = obtainCode(httpServletRequest);
        if (StringUtils.isEmpty(code)) {
            throw new AuthenticationServiceException("验证码为空");
        }
        EmailAuthenticationToken emailAuthenticationToken = new EmailAuthenticationToken(email, code);
        this.setDetails(httpServletRequest, emailAuthenticationToken);
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        //调用authenticationManager进行认证
        return this.getAuthenticationManager().authenticate(emailAuthenticationToken);
    }

    private String obtainEmail(HttpServletRequest request) {
        return request.getParameter(emailNameParam);
    }

    private String obtainCode(HttpServletRequest request) {
        return request.getParameter(emailCodeParam);
    }

    private void setDetails(HttpServletRequest request, EmailAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    /**
     * 设置请求邮箱参数
     *
     * @param emailNameParam
     */
    public void setEmailNameParam(String emailNameParam) {
        this.emailNameParam = emailNameParam;
    }

    /**
     * 设置请求验证码参数
     *
     * @param emailCodeParam
     */
    public void setEmailCodeParam(String emailCodeParam) {
        this.emailCodeParam = emailCodeParam;
    }

    public String getEmailCodeParam() {
        return emailCodeParam;
    }

    public String getEmailNameParam() {
        return emailNameParam;
    }
}
