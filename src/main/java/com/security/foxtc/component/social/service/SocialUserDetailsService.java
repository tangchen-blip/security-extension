package com.security.foxtc.component.social.service;

import com.security.foxtc.component.social.model.SocialUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tangchen
 */
public interface SocialUserDetailsService {

    /**
     * 根据社交登录获取社交的个人信息
     *
     * @param socialToken
     * @return
     */
    SocialUserDetails loadUserBySocialToken(String socialToken);

    /**
     * 根据社交唯一标识获取用户信息
     *
     * @param socialId
     * @return
     */
    UserDetails loadUserBySocialId(String socialId);

}
