package com.yt.bishe.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.deploy.net.HttpResponse;
import com.yt.bishe.entity.*;
import com.yt.bishe.service.*;
import com.yt.bishe.utils.Echarts;
import com.yt.bishe.utils.Export;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private EchartsService es;
    @Autowired
    private OrderService os;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @RequestMapping("/getBar")
    @ResponseBody
    public List<Echarts> getBooks(){
        return es.getBooks();
    }

    @RequestMapping("/getSalesInDay")
    @ResponseBody
    public List<Echarts> getSalesInDay(){
        return es.getSalesInDay();
    }

    @RequestMapping("/exportOrders")
    public void exportOrders(HttpServletResponse response){
        List<ChidOrder> chidOrders = os.getAllChidOrders();
        Export export = new Export();
        try {
            export.exportOrders(chidOrders,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/outLoginAdmin")
    public String outLoginAdmin(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/adminLogin";
    }


    @RequestMapping("/getAllBooks")
    @ResponseBody
    public ModelAndView getAllBooks(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum, ModelAndView modelAndView){
        System.out.println(pageNum);
        PageHelper.startPage(pageNum,10);
        List<Book> books = bookService.getAllBooks();
        PageInfo<Book> bookPage = new PageInfo<>(books);
        modelAndView.addObject("bookPage",bookPage);
        modelAndView.setViewName("sys");
        return modelAndView;
    }

    @RequestMapping("/deleteBook")
    @ResponseBody
    public String deleteBook(@RequestParam int bookId){
        if (bookService.deleteBook(bookId)){
            return "ok";
        }else return "false";
    }

    @RequestMapping("/getAllOrders")
    @ResponseBody
    public ModelAndView getAllOrders(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum, ModelAndView modelAndView){
        PageHelper.startPage(pageNum,10);
        List<ChidOrder> chidOrders = os.getAllChidOrders();
        PageInfo<ChidOrder> orderPage = new PageInfo<>(chidOrders);
        modelAndView.addObject("orderPage",orderPage);
        modelAndView.setViewName("sys_order");
        return modelAndView;
    }

    @RequestMapping("/getAllComments")
    @ResponseBody
    public ModelAndView getAllComments(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum, ModelAndView modelAndView){
        PageHelper.startPage(pageNum,10);
        List<Comment> comments = commentService.getAllComments();
        PageInfo<Comment> commentPage = new PageInfo<>(comments);
        modelAndView.addObject("commentPage",commentPage);
        modelAndView.setViewName("sys_comment");
        return modelAndView;
    }

    @RequestMapping("/deleteComment")
    @ResponseBody
    public String deleteComment(@RequestParam int commentId){
        if (commentService.deleteComment(commentId)){
            return "ok";
        }else return "false";
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public ModelAndView getAllUser(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum, ModelAndView modelAndView){
        PageHelper.startPage(pageNum,10);
        List<User> users = userService.getAllUser();
        PageInfo<User> userPage = new PageInfo<>(users);
        modelAndView.addObject("userPage",userPage);
        modelAndView.setViewName("sys_user");
        return modelAndView;
    }

    @RequestMapping("/reviseUserState")
    @ResponseBody
    public String reviseUserState(String userName,Integer state){
        if (userService.reviseUserState(userName,state)){
            return "ok";
        }else return "false";
    }
}
