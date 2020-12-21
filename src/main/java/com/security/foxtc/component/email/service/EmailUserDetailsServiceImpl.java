package com.security.foxtc.component.email.service;

import com.security.foxtc.component.email.service.EmailUserDetailsService;
import com.security.foxtc.component.social.model.SocialUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author tangchen
 * @date 2020/12/21 11:54 上午
 * @copyright 2020 barm Inc. All rights reserved
 */
public class EmailUserDetailsServiceImpl implements EmailUserDetailsService {
    @Override
    public String loadCodeByEmail(String s) {
        return null;
    }

    @Override
    public UserDetails loadUserByEmail(String s) {
        return null;
    }
}
