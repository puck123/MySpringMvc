package com.user.dao;

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
public interface UserDAO {
    boolean login(UserInfo user);
}
