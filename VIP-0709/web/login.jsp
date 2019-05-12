<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018-07-09
  Time: 20:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/" />
    <title>登录页面</title>
</head>
<body>
<fieldset style="margin:10% auto;width:30%;">
    <legend>登录入口</legend>
    <form action="user/login" method="post">
        <p>账号:<input type="text" name="account"></p>
        <p>密码:<input type="password" name="password"></p>
        <p><input type="checkbox" name="rb" id="rb" value="rb" ><label for="rb">自动登录</label></p>
        <p><input type="submit" value="LOGIN"></p>
    </form>
</fieldset>
</body>
</html>
