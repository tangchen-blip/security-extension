package com.security.foxtc.component.social;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 社交登录的过滤器
 *
 * @author tangchen
 */
public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private Boolean postOnly = true;
    private final static String REQUEST_POST = "POST";
    private static final String REQUEST_URI = "/social/token";
    private static final String REQUEST_METHOD = "POST";
    private String socialParam = "socialToken";

    public SocialAuthenticationFilter() {
        super(new AntPathRequestMatcher(REQUEST_URI, REQUEST_METHOD));
    }

    public SocialAuthenticationFilter(String requestUri, String requestMethod) {
        super(new AntPathRequestMatcher(requestUri, requestMethod));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !REQUEST_POST.equals(httpServletRequest.getMethod())) {
            throw new AuthenticationServiceException("请求方法只能为POST");
        }
        String socialToken = obtainSocialToken(httpServletRequest);
        SocialAuthenticationToken authenticationToken = new SocialAuthenticationToken(socialToken, null);
        this.setDetails(httpServletRequest, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    public String obtainSocialToken(HttpServletRequest request) {
        return request.getParameter(socialParam);
    }

    public void setPostOnly(Boolean postOnly) {
        this.postOnly = postOnly;
    }

    public void setDetails(HttpServletRequest request, SocialAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setSocialParam(String socialParam) {
        this.socialParam = socialParam;
    }
}
