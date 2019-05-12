<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018-06-28
  Time: 20:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/" />
    <title>图书信息列表页面</title>
    <link rel="stylesheet" href="css/main.css" />
</head>
<body>
<table class="tb">
    <caption>${account==null ? "未登录" : "已登录"} |<a href="book/initAdd">【新增】</a></caption>
    <tr>
        <th>编号</th>
        <th>名称</th>
        <th>作者</th>
        <th>价格</th>
        <th>时间</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${books}" var="book">
    <tr>
        <td>${book.bookId}</td>
        <td>${book.bookName}</td>
        <td>${book.bookAuthor}</td>
        <td>${book.bookPrice}</td>
        <td><fmt:formatDate value="${book.bookDate}" pattern="yyyy-MM-dd" /></td>
        <td><a href="book/initUpdate?bookId=${book.bookId}">更新</a>|<a href="book/deleteBook?bookId=${book.bookId}">删除</a></td>
    </tr>
    </c:forEach>
</table>
</body>
</html>
