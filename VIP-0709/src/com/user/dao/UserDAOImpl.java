package com.user.dao;

import com.ruanmou.vip.myspringmvc.annotation.MyAutowired;
import com.ruanmou.vip.myspringmvc.annotation.MyRepository;
import com.ruanmou.vip.orm.core.handler.HandlerTemplate;
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
@MyRepository("userDao")
public class UserDAOImpl implements UserDAO {

    @MyAutowired
    private HandlerTemplate template;

    @Override
    public boolean login(UserInfo user) {
        return template.queryForList(UserInfo.class, user).size() > 0;
    }
}
