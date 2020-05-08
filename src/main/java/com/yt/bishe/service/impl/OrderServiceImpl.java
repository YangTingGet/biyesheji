package com.yt.bishe.service.impl;

import com.yt.bishe.dao.ChidOrderDao;
import com.yt.bishe.dao.OrderDao;
import com.yt.bishe.entity.ChidOrder;
import com.yt.bishe.entity.ChidTradeCar;
import com.yt.bishe.entity.Order;
import com.yt.bishe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ChidOrderDao chidOrderDao;
    @Override
    public String createOrder(String userName,double totalPrice){
        Order order =new Order();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSSsss");
        String orderId = simpleDateFormat.format(Calendar.getInstance().getTime());
        order.setOrderId(orderId);
        order.setTotalPrice(totalPrice);
        order.setUserName(userName);
        if (orderDao.insertOrder(order)){
            return orderId;
        }else return "false";
    }

    @Override
    public void createChidOrder(ChidOrder chidOrder) {
        chidOrderDao.insertChidOrder(chidOrder);
    }

    @Override
    public Order getOrderInfo(String orderId) {
        return orderDao.selectOrderByOrderId(orderId);
    }

    @Override
    public List<ChidOrder> getChidOrderInfo(String orderId) {
        return chidOrderDao.selectChidOrders(orderId);
    }

    @Override
    public void reviseOrderState(String orderId,int state) {
        orderDao.updateOrderState(orderId,state);
    }

    @Override
    public boolean reviseOrderAddress(String address, String orderId) {
        return orderDao.updateOrderAddress(address,orderId);
    }

    @Override
    public List<Order> getUserOrders(String userName) {
        return orderDao.selectOrdersByUserName(userName);
    }

    @Override
    public String getPOrderIdByUserName(String userName) {
        return orderDao.selectPOrderIdByUserName(userName);
    }

    @Override
    public ChidOrder getChidOrder(Integer chidOrderId) {
        return chidOrderDao.selectChidOrder(chidOrderId);
    }

    @Override
    public List<ChidOrder> getShopOrdersByBookId(Integer bookId) {
        return chidOrderDao.selectShopOrdersByBookId(bookId);
    }

    @Override
    public Integer getChidOrderState(String pOrderId) {
        return orderDao.getChidOrderState(pOrderId).getPayState();
    }

    @Override
    public List<ChidOrder> getAllChidOrders() {
        return chidOrderDao.selectAll();
    }
}
