package com.bk.controller;

import com.bk.dao.BookDAO;
import com.bk.dao.impl.BookDAOImpl;
import com.bk.entity.BookEntity;
import com.bk.service.BookService;
import com.ruanmou.vip.myspringmvc.annotation.MyAutowired;
import com.ruanmou.vip.myspringmvc.annotation.MyController;
import com.ruanmou.vip.myspringmvc.annotation.MyRequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:过滤器和监听器
 * </pre>
 *
 * @author gerry
 * @date 2018-06-28
 */

@MyController
@MyRequestMapping(value = "book")
public class BookNewController {
    @MyAutowired("bookService")
    private BookService bookService;

    // 初始化图书信息方法
    @MyRequestMapping("listBook")
    public String list(HttpServletRequest request) {
        List<BookEntity> books = bookService.getBooks();
        // 把books保存到request对象中(保证request对象存储的数据不丢失)
        request.setAttribute("books", books);

        // 转发到主页
        return "forward:/WEB-INF/book/list.jsp";
    }

    // 删除的方法
    @MyRequestMapping("deleteBook")
    public String delete(Integer bookId) {
        // 调用删除的方法
        bookService.deleteBook(bookId);
        // 重定向初始化
        return  "redirect:/book/listBook";
    }

    @MyRequestMapping("initUpdate")
    public String initUpdate(HttpServletRequest request, Integer bookId){
        // 调用根据编号查询图书信息的方法
        BookEntity bookEntity = bookService.getBookById(bookId);
        // 保存request对象中
        request.setAttribute("book", bookEntity);

        // 转发到修改页面
       return "/WEB-INF/book/update.jsp";
    }

    @MyRequestMapping("updateBook")
    public String update(BookEntity bookEntity) {
        // 调用修改数据的方法
        bookService.updateBook(bookEntity);

        return "redirect:/book/listBook";
    }

    // 初始化添加的方法
    @MyRequestMapping("initAdd")
    public String initAdd() {

        // 转发到添加页面
        return "/WEB-INF/book/add.jsp";
    }


    @MyRequestMapping("addBook")
    public String add(BookEntity bookEntity) {
        // 调用修改数据的方法
        bookService.addBook(bookEntity);

        return "redirect:/book/listBook";
    }
}
