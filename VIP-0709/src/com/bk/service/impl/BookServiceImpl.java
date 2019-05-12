package com.bk.service.impl;

import com.bk.dao.BookDAO;
import com.bk.entity.BookEntity;
import com.bk.service.BookService;
import com.ruanmou.vip.myspringmvc.annotation.MyAutowired;
import com.ruanmou.vip.myspringmvc.annotation.MyService;

import java.util.List;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    图像信息管理业务类
 * </pre>
 *
 * @author gerry
 * @date 2018-07-05
 */
@MyService("bookService")
public class BookServiceImpl implements BookService {

    @MyAutowired("bookDao")
    private BookDAO bookDAO;

    @Override
    public void addBook(BookEntity book) {
        bookDAO.addBook(book);
    }

    @Override
    public void deleteBook(Integer bookId) {
        bookDAO.deleteBook(bookId);
    }

    @Override
    public BookEntity getBookById(Integer bookId) {
        return bookDAO.getBookById(bookId);
    }

    @Override
    public void updateBook(BookEntity book) {
        bookDAO.updateBook(book);
    }

    @Override
    public List<BookEntity> getBooks() {
        return bookDAO.getBooks();
    }
}
