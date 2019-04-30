package com.rsun.service;

import com.rsun.dto.User;

/**
 * Created by yfyuan on 2016/9/29.
 */
public interface AuthenticationService {

    /***
     * 获取当前登录的用户
     * @return
     */
    User getCurrentUser();
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    String login(String username, String password);
    String loginRemoteSSO(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 操作结果
     */
    String register(User user);

    /**
     * 刷新密钥
     *
     * @param oldToken 原密钥
     * @return 新密钥
     */
    String refreshToken(String oldToken);

}
