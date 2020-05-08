package com.yt.bishe.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yt.bishe.entity.Book;
import com.yt.bishe.service.BookService;
import com.yt.bishe.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller

public class ElseController {
    @Autowired
    private BookService bookService;
    @Autowired
    private SystemService systemService;
    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView index(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, ModelAndView modelAndView){
        PageInfo<Book> pageInfo=bookService.getAllBooks(pageNum,4);
        modelAndView.addObject("pageInfo",pageInfo);

        PageHelper.startPage(pageNum,4);
        List<Book> books = bookService.findBooksByNew();
        PageInfo<Book> newBooks = new PageInfo<>(books);
        modelAndView.addObject("newBooks",newBooks);
        modelAndView.setViewName("index");
        return modelAndView;
//        modelAndView.setViewName("index");
//        System.out.println("跳转成功");
//        return modelAndView;
    }


    @RequestMapping("/adminCheck")
    @ResponseBody
    public String adminCheck(@RequestParam String adminName, @RequestParam String password, HttpServletRequest request){
        if (systemService.adminLoginCheck(adminName,password)){
            request.getSession().setAttribute("userName",adminName);
            return "1";
        }else return "0";
    }

    @RequestMapping("/adminLogin")
    public String adminLogin(){
        return "login_admin";
    }
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/system")
    public String system(){
        return "system";
    }
    @RequestMapping("/vip")
    public String vip(){ return "vip";}
    @RequestMapping("/sys")
    public String sys(){ return  "sys";}
//    @RequestMapping("/loginAdmin")
//    public String loginAdmin(){ return  "login_admin";}
}


