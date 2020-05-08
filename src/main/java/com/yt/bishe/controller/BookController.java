package com.yt.bishe.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yt.bishe.entity.Book;
import com.yt.bishe.entity.Comment;
import com.yt.bishe.entity.Shop;
import com.yt.bishe.service.BookService;
import com.yt.bishe.service.CommentService;
import com.yt.bishe.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CommentService cService;


    @RequestMapping("/getBookDetails")
    @ResponseBody
    public ModelAndView getBookDetails(@RequestParam int bookId,ModelAndView modelAndView,HttpServletRequest request){
        request.getSession().setAttribute("bookId",bookId);
        Book book = bookService.getBookDetails(bookId);
        List<Comment> comments = cService.getCommentsByBookId(bookId);
        modelAndView.addObject("comments",comments);
        modelAndView.addObject("book",book);
        Shop shop = shopService.getShopInfoByShopId(book.getShopId());
        modelAndView.addObject("shopName",shop.getShopName());
        modelAndView.setViewName("shopbookinfo");
        return modelAndView;
    }

    @RequestMapping("/reviseBook")
    @ResponseBody
    public ModelAndView reviseBook(ModelAndView modelAndView,Book book){
        if (bookService.reviseBookInfo(book)){
            Book book1 = bookService.getBookDetails(book.getBookId());
            modelAndView.addObject("book",book1);
            modelAndView.setViewName("shopbookinfo");
        }
        return modelAndView;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //参数true表示允许日期为空（null、""）
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

    }

    @RequestMapping("/addBook")
    @ResponseBody
    public boolean addBook(Book book,@RequestParam String bookName, @RequestParam String press, @RequestParam String quality, @RequestParam String author, @RequestParam String category, @RequestParam int count, @RequestParam Date publicationDate, @RequestParam double price,@RequestParam String area, HttpServletRequest request){ //,@RequestParam String bookName,@RequestParam String press,@RequestParam String quality,@RequestParam String author,@RequestParam String category,@RequestParam int count,@RequestParam Date publicationDate,@RequestParam double price,
        System.out.println(publicationDate);
        String shopId = (String)request.getSession().getAttribute("shopId");
        book.setBookName(bookName);
        book.setPress(press);
        book.setArea(area);
        book.setQuality(quality);
        book.setAuthor(author);
        book.setCategory(category);
        book.setPublicationDate(publicationDate);
        book.setCount(count);
        book.setPrice(price);
        book.setShopId(shopId);
        return bookService.saveBookInfo(book);
    }

    @Value("D:\\Program Files\\ideaProject\\bookMart\\src\\main\\resources\\static\\images")
    private String filePath;
    @RequestMapping("/addBookPic")
    @ResponseBody
    public ModelAndView addBookPic(@RequestParam("file") MultipartFile file,HttpServletRequest request,ModelAndView modelAndView) throws IOException {
        if(file.isEmpty()){
            throw new NullPointerException("文件为空");
        }
        //获取原始图片的拓展名
        String originalFileName = file.getOriginalFilename();
        String prefix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        //新的文件名
        String newFileName = UUID.randomUUID() +"."+ prefix;
        //封装上传文件位置的全路径
        File targetFile = new File(filePath,newFileName);
        //把本地文件上传到封装上传文件位置的全路径
        file.transferTo(targetFile);
        int bookId = (int)request.getSession().getAttribute("bookId");
        bookService.reviseBookPic(bookId,newFileName);
//        if (bookService.reviseBookPic(bookId,newFileName)){
//            return "redirect:/book/getBookDetails";
//        }else
//            return "redirect:/error";
        modelAndView.setViewName("redirect:/book/getBookDetails?bookId="+bookId);
        return  modelAndView;


    }
    @RequestMapping("/getUserBookDetails")
    @ResponseBody
    public ModelAndView getUserBookDetails(@RequestParam int bookId,ModelAndView modelAndView,HttpServletRequest request){
        request.getSession().setAttribute("bookId",bookId);
        Book book = bookService.getBookDetails(bookId);
        List<Comment> comments = cService.getCommentsByBookId(bookId);
        modelAndView.addObject("comments",comments);
        modelAndView.addObject("book",book);
        Shop shop = shopService.getShopInfoByShopId(book.getShopId());
        modelAndView.addObject("shopName",shop.getShopName());
        modelAndView.setViewName("goodsinfo");
        return modelAndView;
    }



    @RequestMapping("/deleteBook")
    @ResponseBody
    public boolean deleteBook(@RequestParam int bookId){
        if (bookService.deleteBook(bookId)){
            return true;
        }else return false;
    }

    @RequestMapping("/getHotBooks")
    @ResponseBody
    public ModelAndView getHotBooks(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,ModelAndView modelAndView){
        PageHelper.startPage(pageNum,4);
        List<Book> books = bookService.findBooksByHot();
        PageInfo<Book> bookPage= new PageInfo<>(books);
        modelAndView.addObject("bookPage",bookPage);
        modelAndView.setViewName("HotBooks");
        return modelAndView;
    }

    @RequestMapping("/getNewBooks")
    @ResponseBody
    public ModelAndView getNewBooks(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,ModelAndView modelAndView){
        PageHelper.startPage(pageNum,4);
        List<Book> books = bookService.findBooksByNew();
        PageInfo<Book> bookPage = new PageInfo<>(books);
        modelAndView.addObject("bookPage",bookPage);
        modelAndView.setViewName("newBooks");
        return modelAndView;
    }



    @RequestMapping("/searchBook")
    @ResponseBody
    public ModelAndView searchBook(String key,@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,ModelAndView modelAndView){
        System.out.println(key);
        PageHelper.startPage(pageNum,4);
        List<Book> books = bookService.getBooksByKey(key);
        PageInfo<Book> pageInfo = new PageInfo<>(books);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("key",key);
        modelAndView.setViewName("searchResult");
        return modelAndView;
    }

    @RequestMapping("/searchBookByCAndC")
    @ResponseBody
    public ModelAndView searchBookByCAndC(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,String country,String category,ModelAndView modelAndView){
        PageHelper.startPage(pageNum,4);
        List<Book> books = bookService.getBookByCAndC(country,category);
        PageInfo<Book> pageInfo = new PageInfo<>(books);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("country",country);
        modelAndView.addObject("category",category);
        modelAndView.setViewName("searchResult2");
        return modelAndView;
    }

}
