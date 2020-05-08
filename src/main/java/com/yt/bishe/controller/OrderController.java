package com.yt.bishe.controller;

import com.yt.bishe.entity.*;
import com.yt.bishe.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChidTradeCarService ctcService;
    @Autowired
    private ShopService shopService;

    @RequestMapping("/createOrder")
    @ResponseBody
    public String createOrder(@RequestParam int bookId, @RequestParam int count, HttpServletRequest request){
        String userName = (String)request.getSession().getAttribute("userName");
        Integer level = userService.getUserInfo(userName).getVipState();
        Book book =bookService.getBookDetails(bookId);
        double totalP = 0.0;
        if (level == null || level == 0){
            totalP = count*book.getPrice();
        }else if (level == 1){
            totalP = count*book.getPrice() * 0.9;
        }else if (level == 2){
            totalP = count*book.getPrice() * 0.8;
        }else if (level == 3){
            totalP = count*book.getPrice() * 0.7;
        }else if (level == 4){
            totalP = count*book.getPrice() * 0.5;
        }

        ChidOrder chidOrder =new ChidOrder();
        chidOrder.setBookId(bookId);
        chidOrder.setCount(count);
        chidOrder.setPrice(totalP);
        chidOrder.setBookPic(book.getBookAdress());
        chidOrder.setBookName(book.getBookName());

        String result = orderService.createOrder(userName,totalP);
        chidOrder.setpOrderId(result);
        orderService.createChidOrder(chidOrder);
        return result;

    }
    @RequestMapping("/createOrderinCart")
    @ResponseBody
    public String createOrderinCart(final String[] ids,HttpServletRequest request){
        double totalPrice =0.00;
        double off = 0.0;
        String userName = (String)request.getSession().getAttribute("userName");
        User user = userService.getUserInfo(userName);
        Integer level = user.getVipState();
        if (level == 0 || level == null){
            off = 1;
        }else if (level == 1){
            off =0.9;
        }else if (level == 2){
            off =0.8;
        }else if (level == 3){
            off =0.7;
        }else if (level == 4){
            off =0.5;
        }
        for (int i =0 ;i<ids.length;i++){
            ChidTradeCar ctc=ctcService.getCTradeCarById(Integer.parseInt(ids[i]));
            totalPrice=totalPrice+ctc.getPrice()*ctc.getCount()*off;
        }
        String PorderId =orderService.createOrder(userName,totalPrice);
        for (int i =0 ;i<ids.length;i++){
            ChidTradeCar ctc=ctcService.getCTradeCarById(Integer.parseInt(ids[i]));
            ChidOrder chidOrder=new ChidOrder();
            chidOrder.setBookName(ctc.getBookName());
            chidOrder.setBookPic(ctc.getBookPic());
            chidOrder.setCount(ctc.getCount());
            chidOrder.setPrice(ctc.getPrice()*off*ctc.getCount());
            chidOrder.setpOrderId(PorderId);
            chidOrder.setBookId(ctc.getBookId());
            orderService.createChidOrder(chidOrder);
        }
        //生成订单成功后移除子购物车
        for (int i =0 ;i<ids.length;i++){
            ctcService.removeChidTradeCar(Integer.parseInt(ids[i]));
        }
        return PorderId;
    }

    @RequestMapping("/getOrderInfo")
    @ResponseBody
    public ModelAndView getOrderInfo(String orderId,ModelAndView modelAndView,HttpServletRequest request){
        User user =userService.getUserInfo((String)request.getSession().getAttribute("userName"));
        Order order =orderService.getOrderInfo(orderId);
        int count =0;
        List<ChidOrder> chidOrders = orderService.getChidOrderInfo(orderId);
        Iterator<ChidOrder> iterator =chidOrders.iterator();
        while (iterator.hasNext()){
            ChidOrder chidOrder =iterator.next();
            count+=chidOrder.getCount();
        }
        Integer level = user.getVipState();
        modelAndView.addObject("chidOrders",chidOrders);
        modelAndView.addObject("level",level);
        modelAndView.addObject("totalPrice",order.getTotalPrice());
        modelAndView.addObject("totalCount",count);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("jiesuan");
        return modelAndView;
    }

    @RequestMapping("/getOrder0Info")
    @ResponseBody
    public ModelAndView getOrder0Info(String orderId,ModelAndView modelAndView,HttpServletRequest request){
        User user =userService.getUserInfo((String)request.getSession().getAttribute("userName"));
        Double totalPeice=0.00;
        int count =0;
        List<ChidOrder> chidOrders = orderService.getChidOrderInfo(orderId);
        Iterator<ChidOrder> iterator =chidOrders.iterator();
        while (iterator.hasNext()){
            ChidOrder chidOrder =iterator.next();
            count+=chidOrder.getCount();
            totalPeice+=chidOrder.getPrice();
        }
        modelAndView.addObject("chidOrders",chidOrders);
        modelAndView.addObject("totalPrice",totalPeice);
        modelAndView.addObject("totalCount",count);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("OrderInfo");
        return modelAndView;
    }

    @RequestMapping("/reviseOrderAddress")
    @ResponseBody
    public String reviseOrderAddress(@RequestParam String address,@RequestParam String orderId){
        int state = orderService.getOrderInfo(orderId).getPayState();

        if (state != 3) {
            if (orderService.reviseOrderAddress(address, orderId)) {
                return  "1";
            }else return "0";
        }else return "1";

    }


    @RequestMapping("/getUserOrders")
    @ResponseBody
    public ModelAndView getUserOrders(HttpServletRequest request,ModelAndView modelAndView){
        String userName = (String)request.getSession().getAttribute("userName");
        List<Order> orders = orderService.getUserOrders(userName);
        List<Order> orders0 = new ArrayList<>();
        List<Order> orders1 = new ArrayList<>();
        Iterator<Order> iterator =orders.iterator();
        while (iterator.hasNext()){
            Order order = iterator.next();
            if (order.getPayState() == 0){
                orders0.add(order);
            }else if (order.getPayState() == 1){
                orders1.add(order);
            }
        }
        modelAndView.addObject("order0",orders0);
        modelAndView.addObject("order1",orders1);
        modelAndView.addObject("orders",orders);
        modelAndView.setViewName("orders");
        return modelAndView;
    }

    @RequestMapping("/getChidOrderInfo")
    @ResponseBody
    public ModelAndView getChidOrderInfo(int chidOrderId,ModelAndView modelAndView){
        ChidOrder chidOrder=orderService.getChidOrder(chidOrderId);
        modelAndView.addObject("chidOrder",chidOrder);
        modelAndView.setViewName("comment");
        return modelAndView;
    }

    @RequestMapping("/getShopOrders")
    @ResponseBody
    public ModelAndView getShopOrders(HttpServletRequest request,ModelAndView modelAndView){
        String userName = (String)request.getSession().getAttribute("userName");
        String shopId = shopService.getShopInfo(userName).getShopId();
        List<Book> books =bookService.getShopBooks(shopId);
        Iterator<Book> iterator =books.iterator();
        List<ChidOrder>chidOrders =new ArrayList<>();
        while (iterator.hasNext()){
            Book book =iterator.next();
            chidOrders.addAll(orderService.getShopOrdersByBookId(book.getBookId()));
        }
        Iterator<ChidOrder> i =chidOrders.iterator();
        List<ChidOrder> chidOrders1 = new ArrayList<>();
        List<ChidOrder> chidOrders2 = new ArrayList<>();
        while (i.hasNext()){
            ChidOrder co=i.next();
            Integer state = orderService.getChidOrderState(co.getpOrderId());
            if (state == 1){
                chidOrders1.add(co);
            }else if (state == 3 || state == 2){
                chidOrders2.add(co);
            }
        }
        modelAndView.addObject("co1",chidOrders1);
        modelAndView.addObject("co2",chidOrders2);

        modelAndView.setViewName("shopOrders");
        return modelAndView;
    }

    @RequestMapping("/reviseOrderState")
    @ResponseBody
    public String reviseOrderState(@RequestParam Integer cOrderId){
        String pOrderId = orderService.getChidOrder(cOrderId).getpOrderId();
        orderService.reviseOrderState(pOrderId,3);
        return "1";
    }
}
