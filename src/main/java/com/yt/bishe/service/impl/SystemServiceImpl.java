package com.yt.bishe.service.impl;

import com.yt.bishe.dao.AdminDao;
import com.yt.bishe.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private AdminDao adminDao;

    @Override
    public boolean adminLoginCheck(String adminName, String password) {
        String pwd =adminDao.selectPassword(adminName);
        boolean flag = pwd.equals(password);
        return  flag ;
    }
}
