package com.bk.dao;

import com.bk.entity.BookEntity;

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
public interface BookDAO {
    void addBook(BookEntity book); // 新增图书
    void deleteBook(Integer bookId); // 删除图书
    BookEntity getBookById(Integer bookId); // 根据编号获取图书对象
    void updateBook(BookEntity book); // 修改图书信息
    List<BookEntity> getBooks(); // 获取所有的图书新方法
}
