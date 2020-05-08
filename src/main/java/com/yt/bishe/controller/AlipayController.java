package com.yt.bishe.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yt.bishe.dao.VIPOrderDao;
import com.yt.bishe.entity.ChidOrder;
import com.yt.bishe.entity.Order;
import com.yt.bishe.entity.VIPOrder;
import com.yt.bishe.service.BookService;
import com.yt.bishe.service.OrderService;
import com.yt.bishe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AlipayController {
        @Autowired
        private OrderService orderService;
        @Autowired
        private BookService bookService;
        @Autowired
        private VIPOrderDao vDao;
        @Autowired
        private UserService userService;

        private final String APP_ID = "2016101100656903";
        private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIxw7L7lqpkJY7BRYYTR7wyjyPYuwqgOLb4q5uSIZhTz59oH4fBYiKwTFxH3l0829n2GTpH+cCKDGrjO86QpqUiUJ7JH6WYf9zig06b5iTwRg3u3XhApD/1259GUN5OjxaDRV+gLUPqSUYL3yW+DQMYq0b2FSVhGH4aMcM/pWbNq2KGkUktVVgi0MlO7hrb0C5Ofrlt2BTvEOdlqz+9MRjE5xNA+C8cwvpBaAarEUsi648pKFBcBPDWji3EY45DaX2vIgOYciJIuRCY0xf83qrYtMIreIOh4PFQYVmnbfr1flAD+PDneXmBAFsW6jYIt2Wz+9hF7fkOy/eMI5qVA73AgMBAAECggEARqIlbgLlJvPX4Rq7UklhVQ/bPmDjfP+aJ9tkIOerHc2Cg/XBq9t7q8wg8D5ExrSAL6x8UDd9YIvJOsJJOFj04wgPIPCzvo1VEiGUuzyuGn95Ni7ErTJaFwT1tfy573y3dfmTxARLfI7o7Rh4yq3akxrSMrMA20XGIjZZtMg0Ejbj9pi5U5BuIOMao56H5PiEHMxp7Ybeybty4J8z1bEFA/g2KOxf2zSbiVr93ULAyjLlWu6or8H0vZ3mB06zM3OdDLvV16aY/JtD9NI8RBItQiDKe9uvJgwZ9uh0kIsEK80oajXSr6IhewnYvpwCTrL6MMCQ5C4righBh+WBzAXs4QKBgQDQmta6xonFYRiI8uFwm23rqHh+2vxiIpnY09if0DisA7A2ej2nvs4S0T+jqPEUDtFgO9ow1uFcCOrCG3rdfK0aFEWdCReR48Uq9Yg3Y1XpU+E+hJVn9DMQDjCmCPDzP5InmEzjZgxovErSTrGInh6mgooNqRXKWi574sOFlVgtcwKBgQCn2oCwfSO2mFrjiWRE0iZ6olDRru0gIpgrs+ln0v2cOQtGWRgHvIdsBbit3cW8YSQ8aQYy03eJlyLqyJMSJTmKwzPw5PFBLmVQXsBEOt1mGl9ivTvS8/hpZ7cTR0nLP1ygrMRya9+aSobHRbArlSOlGAEBbAKn+mS48WsV1Kc3bQKBgQCMkWpVHehEqzMfRZuIiBRAEpxvzxz2/B3zsH1u457suueJLkJRwQ+YOozJudQKEhog4PQcqQ/fNtsKxf27NoJm6nYmZbQOSQ/Z7O5HdJa1ziIgQLN1A2dNUGKT376OC65vI//b1C5UfV1l7kVPrE03IwvoPKJEYxSjwqDCaWqr5QKBgHoFU6v5LK8ejmP+is+k3agkuw/TVDoaK/kJLtH1Bbw9k76uIdPt75xNhHWQVKCfFZJ83q6wH6P8JLv81z1Hpk8DnIywQdmaerg0SRzQlgYVgeXrnsO564nkhagUGMcR5qo/Lgfn94LYTqYqOOlV37Wyf3ijJkMquXHgvnH+Rxj5AoGAd3UGzdtD+bG5Li1pNGUxRUoMIOH23X53/jGF/4NastowSw5EgnQbGlUKBA1kvlIqLc7RZpcRq+AIOouvvolCl24jb2sY4k8BxxCOFrqVoyXJKFvxkU7qK0s0tsqFkerPvGFujqEJr3GcLokpww7VIGIEFc0vfRDh6aGJDsyX9sI=";
        private final String CHARSET = "UTF-8";
        private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnRs073f0sj4q+I8kZIWzok9NJmOX0VU/axNutq4pNBvrVziEh8tMVaO3HM0Jgj5ZhXOWdbv0Gsw4alD1po/iKNQJ8sKZG6smOU66jGfbTQL5755UVdHKKhSuo1BFfi4yJl1HhyQwTZ+wOXozVX2bdmC1m7G921C509KiGJDye4kVwJuaVSlRHJA0sUU/2MaG2v5vavVpqCLZVqLJm2eyewDsAqNc5El9KD895ahMKi7SPlBo0iIQhYCXEayXtAJKtuFIZ0KyozNZE9tE4T2IbFHXeoV5pheXTTCsjUgdeNWjOqhaucc711mdp06zZfbrLsvMdaod4QeHoRTAYUjVyQIDAQAB";
        //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
        private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
        private final String FORMAT = "JSON";
        //签名方式
        private final String SIGN_TYPE = "RSA2";
        //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
//        private final String NOTIFY_URL = "localhost:8080/index";
        private final String NOTIFY_URL = "/notifyUrl";
        //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
//        private final String RETURN_URL = "localhost:8080/checkPay";
        private final String RETURN_URL = "/checkPay";
        private final String RETURN_URL2 = "/VIPPay";

        @RequestMapping("/alipay")
        @ResponseBody
        public void alipay(String orderId, HttpServletResponse response)throws Exception{
                //实例化客户端,填入所需参数
                AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
                AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
                //在公共参数中设置回跳和通知地址
                request.setReturnUrl("http://localhost:8080"+RETURN_URL);
                request.setNotifyUrl("http://localhost:8080"+NOTIFY_URL);
                Order order=orderService.getOrderInfo(orderId);
                double price = order.getTotalPrice();
                String subject ="图书购买";
                String body = "二手书";
                request.setBizContent("{\"out_trade_no\":\""+ orderId +"\","
                        + "\"total_amount\":\""+ price +"\","
                        + "\"subject\":\""+ subject +"\","
                        + "\"body\":\""+ body +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
                String form = "";
                try {
                        form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
                } catch (AlipayApiException e) {
                        e.printStackTrace();
                }
                response.setContentType("text/html;charset=" + CHARSET);
                response.getWriter().write(form);// 直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();

        }

        @RequestMapping(value = "/checkPay" ,method = RequestMethod.GET)
        public String checkPay(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView)throws IOException,AlipayApiException {
                //获取支付宝的反馈信息
                Map<String, String> params = new HashMap<String, String>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                        String name = (String) iter.next();
                        String[] values = (String[]) requestParams.get(name);
                        String valueStr = "";
                        for (int i = 0; i < values.length; i++) {
                                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                        }
                        // 乱码解决，这段代码在出现乱码时使用
                        valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
                        params.put(name, valueStr);
                }

                System.out.println(params);//查看参数都有哪些
                boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
                //验证签名通过
                if (signVerified) {
                        // 商户订单号
                        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

                        // 支付宝交易号
                        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

                        // 付款金额
                        String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

                        System.out.println("商户订单号=" + out_trade_no);
                        System.out.println("支付宝交易号=" + trade_no);
                        System.out.println("付款金额=" + total_amount);

                        orderService.reviseOrderState(out_trade_no,1);
                        List<ChidOrder> chidOrders = orderService.getChidOrderInfo(out_trade_no);
                        Iterator<ChidOrder> iterator =chidOrders.iterator();
                        while (iterator.hasNext()){
                                ChidOrder c =iterator.next();
                                bookService.reviseBookCount(c.getBookId(),c.getCount());
                                bookService.reviseBooksSales(c.getBookId(),c.getCount());
                        }

                        return "redirect:/index";
                }else return "payFail";

        }

//        @RequestMapping("/notifyUrl")
////        public void notifyUrl(HttpServletRequest request) throws Exception {
////                System.out.printf("支付成功2");
////                // 获取支付宝GET过来反馈信息
////                Map<String, String> params = new HashMap<String, String>();
////                Map<String, String[]> requestParams = request.getParameterMap();
//////                for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//////                        String name = (String) iter.next();
//////                        String[] values = (String[]) requestParams.get(name);
//////                        String valueStr = "";
//////                        for (int i = 0; i < values.length; i++) {
//////                                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//////                        }
//////                        params.put(name, valueStr);
//////                }
////
////              // 调用SDK验证签名
////                boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
////                if (signVerified) { // 验证成功 更新订单信息
////                        System.out.println("异步通知成功");
////                        // 商户订单号
////                        String out_trade_no = request.getParameter("out_trade_no");
////                        // 交易状态
////                        String trade_status = request.getParameter("trade_status");
////                        // 修改数据库
////                        orderService.reviseOrderState(out_trade_no,1);
////                        List<ChidOrder> chidOrders = orderService.getChidOrderInfo(out_trade_no);
////                        Iterator<ChidOrder> iterator =chidOrders.iterator();
////                        while (iterator.hasNext()){
////                                ChidOrder c =iterator.next();
////                                bookService.reviseBookCount(c.getBookId(),c.getCount());
////                        }
////
////                } else {
////                        System.out.println("异步通知失败");
////                }
////        }

        @RequestMapping("/VIPalipay")
        @ResponseBody
        public void VIPalipay(Integer level, HttpServletResponse response,HttpServletRequest r)throws Exception{
                //实例化客户端,填入所需参数
                AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
                AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
                //在公共参数中设置回跳和通知地址
                request.setReturnUrl("http://localhost:8080"+RETURN_URL2);
//                request.setNotifyUrl("http://localhost:8080"+NOTIFY_URL);
                String userName = (String) r.getSession().getAttribute("userName");

                String subject ="会员开通";
                String body = "";
                double price = 0.0;
                if (level == 1){
                        body = "普通会员：价格200￥";
                        price = 200.0;
                }else if ( level ==2){
                        body = "精品会员：价格400￥";
                        price = 400.0;
                }else if (level == 3){
                        body = "铂金会员：价格600￥";
                        price = 600.0;
                }else if (level == 4){
                        body = "钻石会员：价格1000￥";
                        price = 1000.0;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSSsss");
                String orderId = simpleDateFormat.format(Calendar.getInstance().getTime());
                vDao.insertVIPOrder(userName,price,orderId);
                request.setBizContent("{\"out_trade_no\":\""+orderId +"\","
                        + "\"total_amount\":\""+ price +"\","
                        + "\"subject\":\""+ subject +"\","
                        + "\"body\":\""+ body +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
                String form = "";
                try {
                        form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
                } catch (AlipayApiException e) {
                        e.printStackTrace();
                }
                response.setContentType("text/html;charset=" + CHARSET);
                response.getWriter().write(form);// 直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();

        }

        @RequestMapping(value = "/VIPPay" ,method = RequestMethod.GET)
        public ModelAndView VIPPay(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView)throws IOException,AlipayApiException {
                //获取支付宝的反馈信息
                Map<String, String> params = new HashMap<String, String>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                        String name = (String) iter.next();
                        String[] values = (String[]) requestParams.get(name);
                        String valueStr = "";
                        for (int i = 0; i < values.length; i++) {
                                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                        }
                        // 乱码解决，这段代码在出现乱码时使用
                        valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
                        params.put(name, valueStr);
                }

                System.out.println(params);//查看参数都有哪些
                boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
                //验证签名通过
                if (signVerified) {
                        // 商户订单号
                        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

                        // 支付宝交易号
                        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

                        // 付款金额
                        String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

                        System.out.println("商户订单号=" + out_trade_no);
                        System.out.println("支付宝交易号=" + trade_no);
                        System.out.println("付款金额=" + total_amount);
                        VIPOrder vipOrder= vDao.getVIPOrder(out_trade_no);
                        double price = vipOrder.getPrice();
                        int level = 0;
                        if (price == 200.0){
                                level = 1;
                        }else if ( price == 400.0){
                                level = 2;
                        }else if (price == 600.0){
                                level = 3;
                        }else if (price == 1000.0){
                                level = 4;
                        }

                        userService.reviseVIPState(vipOrder.getUser_name(),level);
                        //完善订单表
                        Integer vipId = userService.getVIPID(vipOrder.getUser_name());
                        vDao.updateVIPId(vipOrder.getOrder_id(),vipId);

                        modelAndView.addObject("vipOrder",vipOrder);
                        modelAndView.setViewName("vipSuccess");
                        return modelAndView;
                }else {
                        modelAndView.setViewName("payFail");
                        return modelAndView;
                }

        }

}
