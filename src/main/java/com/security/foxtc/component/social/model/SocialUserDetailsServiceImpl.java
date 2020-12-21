package com.security.foxtc.component.social.model;

import com.security.foxtc.UserDetailServiceImpl;
import com.security.foxtc.component.social.model.SocialUserDetails;
import com.security.foxtc.component.social.service.SocialUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author tangchen
 * @date 2020/12/21 11:55 上午
 * @copyright 2020 barm Inc. All rights reserved
 */

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {
    @Override
    public SocialUserDetails loadUserBySocialToken(String s) {
        return null;
    }

    @Override
    public UserDetails loadUserBySocialId(String s) {
        return null;
    }
}
