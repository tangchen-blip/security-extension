package com.security.foxtc.handler;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登出成功处理器
 *
 * @author tangchen
 */
public class BaseLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = request.getParameter("access_token");
        accessToken = StringUtils.isEmpty(accessToken) ? request.getHeader("Authorization") : accessToken;
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken = accessToken.replaceFirst("Bearer ", "");
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
            if (oAuth2AccessToken != null) {
                System.out.println("----access_token是：" + oAuth2AccessToken.getValue());
                tokenStore.removeAccessToken(oAuth2AccessToken);
                OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
                tokenStore.removeRefreshToken(oAuth2RefreshToken);
                tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String type = request.getHeader("Accept");
        if (!type.contains("text/html")) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "退出成功");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } else {
            super.onLogoutSuccess(request, response, authentication);
        }
    }
}
