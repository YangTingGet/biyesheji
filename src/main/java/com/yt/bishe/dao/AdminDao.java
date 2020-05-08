package com.yt.bishe.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminDao {
    /**
     * 查询用户密码
     * @param adminName
     */
    String selectPassword(String adminName);
}
