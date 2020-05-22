package com.security.foxtc.component.social.model;

import lombok.Data;

/**
 * 社交用户信息
 *
 * @author tangchen
 */
@Data
public class SocialUserDetails {
    private String socialRefreshToken;
    private String socialAccessToken;
    private String socialId;
}
