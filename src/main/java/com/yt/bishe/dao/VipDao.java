package com.yt.bishe.dao;

import com.yt.bishe.entity.Vip;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VipDao {
    void insertVip(Vip vip);

    void updateVip(String userName, Integer vipLevel);

    Integer selectVIPIdByUserName(String userName);

    void deleteVIP(String userName);

    Vip getVipInfo(String userName);
}
