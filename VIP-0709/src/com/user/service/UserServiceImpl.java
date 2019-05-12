package com.user.service;

import com.ruanmou.vip.myspringmvc.annotation.MyAutowired;
import com.ruanmou.vip.myspringmvc.annotation.MyService;
import com.user.dao.UserDAO;
import com.user.entity.UserInfo;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 *
 * @author gerry
 * @date 2018-07-09
 */
@MyService("userService")
public class UserServiceImpl implements UserService {

    @MyAutowired("userDao")
    private UserDAO userDAO;

    @Override
    public boolean login(UserInfo user) {
        return userDAO.login(user);
    }
}
