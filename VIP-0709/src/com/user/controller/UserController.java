package com.user.controller;

import com.ruanmou.vip.myspringmvc.annotation.MyAutowired;
import com.ruanmou.vip.myspringmvc.annotation.MyController;
import com.ruanmou.vip.myspringmvc.annotation.MyRequestMapping;
import com.user.entity.UserInfo;
import com.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 *
 * @author gerry
 * @date 2018-07-09
 */
@MyController("userController")
@MyRequestMapping("user")
public class UserController {

    @MyAutowired("userService")
    private UserService userService;

    @MyRequestMapping("login")
    public String login(HttpServletRequest request ,HttpServletResponse response, HttpSession session, UserInfo user, String rb) throws IOException {
        System.out.println(user.getAccount()  + "," + user.getPassword());

        boolean flag = userService.login(user);

        if (flag) {
            // 把登录成功的用户账号保存起来
            session.setAttribute("account", user.getAccount());
            // 判断是否勾选记住密码
            if (rb != null) {
                // 创建Cookie对象保存登录信息
                Cookie userCookie = new Cookie("rb", user.getAccount()+"_"+user.getPassword());
                // 设置cookie有效时间
                userCookie.setMaxAge(60*60*24); // 1天有效
                // 设置访问路径
                userCookie.setPath(request.getContextPath()+"/");
                // 发送客户端保存
                response.addCookie(userCookie);

            } else {
                // 创建Cookie对象保存登录信息
                Cookie userCookie = new Cookie("rb", null);
                // 设置cookie有效时间
                userCookie.setMaxAge(0);
                // 设置访问路径
                userCookie.setPath(request.getContextPath()+"/");
                // 发送客户端保存
                response.addCookie(userCookie);
            }

            return "redirect:/book/listBook";
        } else {
            String url = request.getContextPath()+"/login.jsp";
            response.getWriter().print("<script>alert('登录失败');location.href='"+url+"';</script>");
        }

        return null;
    }
}
