package com.yt.bishe.service;

import com.yt.bishe.entity.User;
import com.yt.bishe.entity.Vip;

import java.util.List;


public interface UserService {
    /**
     * 存储用户信息
     */
    boolean saveUserInfo(User user);

    /**
     * 登录校验密码
     */
    String loginCheck(String userName, String password);

    /**
     * 校验用户名
     */
    boolean checkUserName(String userName);

    /**
     * 获取用户信息
     */
    User getUserInfo(String userName);

    /**
     * 修改用户信息
     */
    boolean reviseUserInfo(User user);

    /**
     * 修改用户密码
     */
    boolean revisePasswrod(String userName, String password);

    /**
     * 修改用户的VIP状态
     */
    void reviseVIPState(String userName, Integer state);

    /**
     * 获取所有用户的信息
     * @return
     */
    List<User> getAllUser();

    /**
     * 修改账号状态
     * @param userName
     * @return
     */
    boolean reviseUserState(String userName, int state);
    /**
     * 生成用户VIP数据
     */
    void addUserVIPInfo(String userName, int level);

    /**
     * 获取用户vip编号
     */
    Integer getVIPID(String userName);

    /**
     * 获取vip信息
     */
    Vip getVIPInfo(String userName);

    /**
     * 会员信息过期进行删除
     */
    void deleteVIPInfo(String userName);
}
