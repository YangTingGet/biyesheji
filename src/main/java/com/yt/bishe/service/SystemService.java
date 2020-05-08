package com.yt.bishe.service;

public interface SystemService {
    /**
     * 登录校验密码
     */
    boolean adminLoginCheck(String adminName, String password);
}
