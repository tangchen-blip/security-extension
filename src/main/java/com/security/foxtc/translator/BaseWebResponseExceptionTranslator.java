package com.security.foxtc.translator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * 自定义登录异常返回格式
 *
 * @author tangchen
 */
@Component
public class BaseWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Value("${security.oauth2.login.fail.code:code}")
    private String codeParam;
    @Value("security.oauth2.login.fail.msg:msg")
    private String msgParam;

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

        ResponseEntity<OAuth2Exception> entity = super.translate(e);

        CustomOauth2Exception customOauth2Exception = new CustomOauth2Exception(entity.getBody().getMessage());

        return ResponseEntity.status(entity.getStatusCode()).body(customOauth2Exception);
    }

    private class CustomOauthExceptionSerializer extends StdSerializer<CustomOauth2Exception> {

        private static final long serialVersionUID = 1478842053473472921L;

        public CustomOauthExceptionSerializer() {
            super(CustomOauth2Exception.class);
        }

        @Override
        public void serialize(CustomOauth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField(codeParam, String.valueOf(value.getHttpErrorCode()));
            gen.writeStringField(msgParam, value.getMessage());
            if (value.getAdditionalInformation() != null) {
                for (Map.Entry<String, String> entry :
                        value.getAdditionalInformation().entrySet()) {
                    String key = entry.getKey();
                    String add = entry.getValue();
                    gen.writeStringField(key, add);
                }
            }
            gen.writeEndObject();
        }
    }


    @JsonSerialize(using = CustomOauthExceptionSerializer.class)
    private class CustomOauth2Exception extends OAuth2Exception {

        public CustomOauth2Exception(String msg, Throwable t) {
            super(msg, t);
        }

        public CustomOauth2Exception(String msg) {
            super(msg);
        }
    }
}
