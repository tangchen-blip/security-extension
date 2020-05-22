package com.security.foxtc.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录失败处理器
 *
 * @author tangchen
 */


@Component
@Slf4j
public class BaseAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${security.oauth2.login.fail.code:code}")
    private String codeParam;
    @Value("security.oauth2.login.fail.msg:msg")
    private String msgParam;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String type = request.getHeader("Accept");
        if (!type.contains("text/html")) {
            response.setContentType("application/json;charset=UTF-8");
            Writer writer = response.getWriter();
            response.setStatus(400);
            Map<String, Object> errorMap = new HashMap<>(2);
            errorMap.put(codeParam, 400);
            errorMap.put(msgParam, exception.getMessage());
            writer.write(JSON.toJSONString(errorMap));
            log.info(exception.getMessage());
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
        //super.onAuthenticationFailure(request, response, exception);
    }
}
