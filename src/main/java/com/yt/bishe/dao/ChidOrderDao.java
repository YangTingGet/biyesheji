package com.yt.bishe.dao;

import com.yt.bishe.entity.ChidOrder;
import com.yt.bishe.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChidOrderDao {
    boolean insertChidOrder(ChidOrder chidOrder);
    List<ChidOrder> selectChidOrders(String orderId);
    ChidOrder  selectChidOrder(Integer chidOrderId);

    List<ChidOrder> selectShopOrdersByBookId(Integer bookId);

    List<ChidOrder> selectAll();
}
