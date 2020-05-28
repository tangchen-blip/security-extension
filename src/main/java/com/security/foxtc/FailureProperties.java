package com.security.foxtc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tangchen
 */
@ConfigurationProperties(prefix = "security.failure")
@Data
public class FailureProperties {

    private String statusParam = "code";
    private String msgParam = "msg";

}
