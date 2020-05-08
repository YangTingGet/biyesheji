package com.yt.bishe.dao;

import com.yt.bishe.entity.VIPOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VIPOrderDao {
    void insertVIPOrder(String userName, double price, String orderId);
    VIPOrder getVIPOrder(String orderId);
    void updateVIPId(String orderId, Integer vipId);
}
