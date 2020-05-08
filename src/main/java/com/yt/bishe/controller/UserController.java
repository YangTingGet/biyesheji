package com.yt.bishe.controller;


import ch.qos.logback.core.util.DatePatternToRegexUtil;
import com.yt.bishe.entity.Shop;
import com.yt.bishe.entity.TradeCar;
import com.yt.bishe.entity.User;
import com.yt.bishe.entity.Vip;
import com.yt.bishe.service.ShopService;
import com.yt.bishe.service.TradeCarService;
import com.yt.bishe.service.UserService;
import com.yt.bishe.utils.MailUtil;
import com.yt.bishe.utils.Md5;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param request
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public int UserLogin(HttpServletRequest request,String userName,String password){
        String Pwd = Md5.getMd5(password);
        String flag = userService.loginCheck(userName,Pwd);
        if (flag == "0"){
            request.getSession().setAttribute("userName",userName);
            int vipState = userService.getUserInfo(userName).getVipState();
            if (vipState != 0){
            return 3;}
            else return 0;
        }else if (flag == "1"){
            return 1;
        }else
            return 2;
    }

    /**
     * 用户名检测
     * @param name
     * @return
     */
    @RequestMapping("/do_checkUserName")
    @ResponseBody
    public int checkRegisterName( String name){

        if (userService.checkUserName(name))
            return 0;
        else{
            return 1;
        }

    }

    /**
     * 用户注册
     */
    @Autowired
    private ShopService shopService;
    @Autowired
    private TradeCarService tradeCarService;
    @RequestMapping("/doRegist")
    @ResponseBody
    public ModelAndView doRegist(@RequestParam String userName, @RequestParam String password,
                                 @RequestParam String telephone, @RequestParam String email,
                                 @RequestParam String address, ModelAndView modelAndView, User user, Shop shop, HttpServletRequest request){



        user.setUserName(userName);
        user.setPassword(Md5.getMd5(password));
        user.setEmail(email);
        user.setTelephone(telephone);
        user.setAddress(address);
        if ( userService.saveUserInfo(user)){
            //自动为用户生成商铺
            shop.setUserName(userName);
            shop.setShopId(UUID.randomUUID().toString());
            shopService.saveShopInfo(shop);
            //自动为用户生成购物车
            TradeCar tradeCar = new TradeCar();
            tradeCar.setUserName(userName);
            tradeCar.setTradeCarId(UUID.randomUUID().toString()+userName);
            tradeCarService.registerTradeCar(tradeCar);


            request.getSession().setAttribute("userName",userName);
            modelAndView.addObject("userName",userName);
            modelAndView.setViewName("registSuccess");

        }return modelAndView;

    }

    /**
     * 查询用户信息返回给前台
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public ModelAndView getUserInfo(ModelAndView modelAndView,HttpServletRequest request){
        String userName = (String)request.getSession().getAttribute("userName");
        User user = userService.getUserInfo(userName);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("user");

        return modelAndView;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //参数true表示允许日期为空（null、""）
        binder.registerCustomEditor(java.sql.Date.class, new CustomDateEditor(dateFormat, true));

    }
    /**
     * 修改用户的信息
     */
    @RequestMapping("/reviseUserInfo")
    @ResponseBody
    public ModelAndView reviseUserInfo(User user,ModelAndView modelAndView){
        if(userService.reviseUserInfo(user)){
            modelAndView.addObject("user",user);
            modelAndView.setViewName("user");
        }
        return modelAndView;
    }

    @Autowired
    MailUtil mailUtil;
    /**
     * 获取邮箱验证码
     */
    @RequestMapping("/getCodeByEmail")
    @ResponseBody
    public String getCodeByEmail(HttpServletRequest request){
        String userName = (String) request.getSession().getAttribute("userName");
        String mail = userService.getUserInfo(userName).getEmail();
        String code = String.valueOf(new Random().nextInt(899999)+100000);
        String message = "您的验证码为："+code;
        try {
            mailUtil.sendCodetoMail(mail,message);
            System.out.println("发送成功");
        }catch (Exception e){
            System.out.println(e);
            return "";
        }

        return code;
    }

    /**
     * 修改用户密码
     * @param pwd
     */
    @RequestMapping("/revisePassword")
    @ResponseBody
    public String revisePassword(@RequestParam String pwd,HttpServletRequest request){
        System.out.println("进入修改密码");
        String userName = (String) request.getSession().getAttribute("userName");
        String password = Md5.getMd5(pwd);
        userService.revisePasswrod(userName,password);
        System.out.println("修改完成");
        return "ok";
    }

    @RequestMapping("/getUserNameInSession")
    @ResponseBody
    public ModelAndView getUserNameInSession(HttpServletRequest request,ModelAndView modelAndView){
        String userName = (String)request.getSession().getAttribute("userName");
        System.out.println(userName);
        modelAndView.addObject("userName",userName);
        modelAndView.setViewName("top-part");
        return modelAndView;
    }

    @RequestMapping("/reviseVIPState")
    @ResponseBody
    public String reviseVIPState(@RequestParam Integer state,HttpServletRequest request){
        String userName = (String)request.getSession().getAttribute("userName");
        userService.reviseVIPState(userName,state);
        return "ok";
    }

    @RequestMapping("/checkUserVipTime")
    @ResponseBody
    public String checkUserVipTime(HttpServletRequest request){
        String userName = (String) request.getSession().getAttribute("userName");
        Integer vipId = userService.getVIPID(userName);
        if (vipId ==null){
            return "0";
        }else {
            Vip vip = userService.getVIPInfo(userName);
            Date nowDay = new Date();
            if (nowDay.before(vip.getEndTime())){
                return "true";//判断为vip未过期
            }else {
                userService.deleteVIPInfo(userName);
                return "false"; // vip已过期
            }
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();//移除session中的所有数据
        return "redirect:/index";
    }

}
