package com.yt.bishe.dao;


import com.yt.bishe.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderDao {
    boolean insertOrder(Order order);
    Order selectOrderByOrderId(String orderId);
    void updateOrderState(String orderId, int state);
    boolean updateOrderAddress(String address, String orderId);

    List<Order> selectOrdersByUserName(String userName);

    String selectPOrderIdByUserName(String userName);

    Order getChidOrderState(String pOrderId);
}
