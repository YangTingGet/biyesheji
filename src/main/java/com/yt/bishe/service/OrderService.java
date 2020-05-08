package com.yt.bishe.service;

import com.yt.bishe.entity.ChidOrder;
import com.yt.bishe.entity.ChidTradeCar;
import com.yt.bishe.entity.Order;

import java.util.List;

public interface OrderService {
    String createOrder(String userName, double totalPrice);
    void createChidOrder(ChidOrder chidOrder);
    Order getOrderInfo(String orderId);
    List<ChidOrder>getChidOrderInfo(String orderId);
    void reviseOrderState(String orderId, int state);
    boolean reviseOrderAddress(String address, String orderId);


    List<Order> getUserOrders(String userName);
    String getPOrderIdByUserName(String userName);
    ChidOrder  getChidOrder(Integer chidOrderId);

    List<ChidOrder> getShopOrdersByBookId(Integer bookId);

    Integer getChidOrderState(String pOrderId);

    List<ChidOrder> getAllChidOrders();
}
