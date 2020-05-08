package com.yt.bishe.controller;

import com.yt.bishe.entity.Comment;
import com.yt.bishe.service.CommentService;
import com.yt.bishe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/addComment")
    @ResponseBody
    public String addComment(@RequestParam Integer bookId,@RequestParam String content, @RequestParam String pId,HttpServletRequest request,Comment comment){
        String userName=(String)request.getSession().getAttribute("userName");
        comment.setBookId(bookId);
        comment.setUserName(userName);
        comment.setContent(content);
        if (commentService.addComment(comment)){
            orderService.reviseOrderState(pId,2);
            return "1";
        }else
        return "0";
    }
}
