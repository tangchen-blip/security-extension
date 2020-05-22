package com.security.foxtc.component.email.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 邮箱验证码获取Service
 *
 * @author tangchen
 */
public interface EmailUserDetailsService {

    /**
     * 根据邮箱获取验证码
     *
     * @param email
     * @return
     */
    String loadCodeByEmail(String email);

    /**
     * 根据邮箱账号获取用户信息
     *
     * @param email
     * @return
     */
    UserDetails loadUserByEmail(String email);
}
