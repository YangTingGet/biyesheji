package com.yt.bishe.service.impl;

import com.yt.bishe.dao.UserDao;
import com.yt.bishe.dao.VipDao;
import com.yt.bishe.entity.User;
import com.yt.bishe.entity.Vip;
import com.yt.bishe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    private VipDao vipDao;

    @Override
    public boolean saveUserInfo(User user){

       return userDao.insertUserInfo(user);
    }

    @Override
    public String loginCheck(String userName,String password){

        User user = userDao.queryUserInfo(userName);
        System.out.println(user.getState());
        if (user.getState()!=0){
            return "1";
        }
        else if (password.equals(user.getPassword()))
            return "0";
        else
            return "2";
    }

    @Override
    public boolean checkUserName(String userName){

        if (userDao.queryUserInfo(userName) == null)
            return false;
        else
            return true;
    }

    @Override
    public User getUserInfo(String userName) {
        return userDao.queryUserInfo(userName);
    }

    @Override
    public boolean reviseUserInfo(User user){
        return userDao.updateUserInfo(user);
    }

    @Override
    public boolean revisePasswrod(String userName,String password){
        return userDao.updatePassword(userName,password);
    }

    @Override
    public void reviseVIPState(String userName, Integer state) {
        Integer vipId = getVIPID(userName);
        if (vipId == null){
            //自动生成用户VIP数据
            addUserVIPInfo(userName,state);
        }else {
            //修改用户已有的VIP数据
            vipDao.updateVip(userName,state);
        }
        userDao.updateVIPState(userName,state);
    }

    @Override
    public boolean reviseUserState(String userName,int state) {
        return userDao.updateUserState(userName,state);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.findAllUser();
    }

    @Override
    public void addUserVIPInfo(String userName,int level) {
        Vip vip =new Vip();
        vip.setUserName(userName);
        vip.setVipLevel(0);
        vipDao.insertVip(vip);
    }

    @Override
    public Integer getVIPID(String userName) {
        return vipDao.selectVIPIdByUserName(userName);
    }

    @Override
    public Vip getVIPInfo(String userName) {
        return vipDao.getVipInfo(userName);
    }

    @Override
    public void deleteVIPInfo(String userName) {
        vipDao.deleteVIP(userName);
    }
}
