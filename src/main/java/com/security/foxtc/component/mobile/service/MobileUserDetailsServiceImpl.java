package com.security.foxtc.component.mobile.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author tangchen
 * @date 2020/12/21 11:57 上午
 * @copyright 2020 barm Inc. All rights reserved
 */
public class MobileUserDetailsServiceImpl implements MobileUserDetailsService {
    @Override
    public String loadCodeByMobile(String s) {
        return null;
    }

    @Override
    public UserDetails loadUserByMobile(String s) {
        return null;
    }
}
