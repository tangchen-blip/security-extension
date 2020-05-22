package com.security.foxtc.component.mobile.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 邮箱验证码获取Service
 *
 * @author tangchen
 */
public interface MobileUserDetailsService {

    /**
     * 根据手机号码获取验证码
     *
     * @param mobile
     * @return
     */
    String loadCodeByMobile(String mobile);

    /**
     * 根据手机号码获取用户信息
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);
}
