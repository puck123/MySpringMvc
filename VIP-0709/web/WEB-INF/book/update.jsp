<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018-06-28
  Time: 21:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/" />
    <title>修改</title>
    <link rel="stylesheet" href="css/main.css" />
</head>
<body>
<form action="book/updateBook" method="post">
    <table class="tb">
        <tr>
            <th>编号</th>
            <td><input type="text" readonly="readonly" name="bookId" value="${book.bookId}"></td>
        </tr>
        <tr>
            <th>名称</th>
            <td><input type="text" name="bookName" value="${book.bookName}"></td>
        </tr>
        <tr>
            <th>作者</th>
            <td><input type="text" name="bookAuthor" value="${book.bookAuthor}"></td>
        </tr>
        <tr>
            <th>价格</th>
            <td><input type="text" name="bookPrice" value="${book.bookPrice}"></td>
        </tr>
        <tr>
            <th>日期</th>
            <td><input type="date" name="bookDate" value="<fmt:formatDate value="${book.bookDate}" pattern="yyyy-MM-dd" />"></td>
        </tr>
        <tr>
            <td colspan="2">
                <button>提交</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
