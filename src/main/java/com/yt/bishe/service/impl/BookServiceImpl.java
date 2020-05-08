package com.yt.bishe.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yt.bishe.dao.BookDao;
import com.yt.bishe.entity.Book;
import com.yt.bishe.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookDao bookDao;

    @Override
    public List<Book> getShopBooks(String shopId){
        return bookDao.findBooksByShopId(shopId);
    }
    @Override
    public Book getBookDetails(int bookId){
        return bookDao.selectBookByBookId(bookId);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.selectAllBooks();
    }

    @Override
    public boolean saveBookInfo(Book book) {
        return bookDao.insertBookInfo(book);
    }

    @Override
    public List<Book> findBooks(String bookName) {
        return bookDao.selectBooksByName(bookName);
    }

    @Override
    public List<Book> findBooksByCategory() {
        return null;
    }

    @Override
    public List<Book> findBooksByHot() {

        return bookDao.selectBookBySales();
    }

    @Override
    public List<Book> findBooksByNew() {
        return bookDao.selectBookByNew();
    }

    @Override
    public List<Book> getBooksByKey(String key) {
        return bookDao.selectBooksByKey(key);
    }

    @Override
    public List<Book> getBookByCAndC(String country, String category) {
        return bookDao.selectBookByCAndC(country,category);
    }

    @Override
    public void reviseBookCount(int bookId,int count) {
        bookDao.updateBookCount(bookId,count);
    }

    @Override
    public void reviseBooksSales(int bookId, int count) {
        bookDao.updateBookSales(bookId,count);
    }

    @Override
    public boolean reviseBookState(int bookId) {
        return false;
    }

    @Override
    public boolean reviseBookInfo(Book book){
       return bookDao.updateBookInfo(book);
    }

    @Override
    public boolean deleteBook(int bookId){
        return bookDao.deleteBook(bookId);
    }

    @Override
    public void reviseBookPic(int bookId, String bookPic){
        try {bookDao.updateBookPic(bookId, bookPic);
        }catch (Exception e){
            System.out.println(e);
        }
    }



    @Override
    public PageInfo<Book> getAllBooks(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Book> books=bookDao.selectBookByPage();
        PageInfo<Book> pageInfo =new PageInfo<>(books,pageSize);
        return pageInfo;
    }
}
