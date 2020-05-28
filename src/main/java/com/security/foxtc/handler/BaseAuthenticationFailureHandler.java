package com.security.foxtc.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

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

@Slf4j
public class BaseAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String ACCEPT_TYPE_HTML = "text/html";
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private String codeParam;

    private String msgParam;

    public void setCodeParam(String codeParam) {
        this.codeParam = codeParam;
    }

    public void setMsgParam(String msgParam) {
        this.msgParam = msgParam;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String type = request.getHeader("Accept");
        if (!type.contains(ACCEPT_TYPE_HTML)) {
            response.setContentType(CONTENT_TYPE_JSON);
            Writer writer = response.getWriter();
            response.setStatus(400);
            Map<String, Object> errorMap = new HashMap<>(2);
            errorMap.put(codeParam, 400);
            errorMap.put(msgParam, exception.getMessage());
            writer.write(JSON.toJSONString(errorMap));
            log.debug(exception.getMessage());
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
