package com.user.entity;

import com.ruanmou.vip.orm.annotation.Column;
import com.ruanmou.vip.orm.annotation.PK;
import com.ruanmou.vip.orm.annotation.Table;

import java.io.Serializable;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    用户实体
 * </pre>
 *
 * @author gerry
 * @date 2018-07-09
 */
@Table("t_user")
public class UserInfo implements Serializable {

    @PK // 标注为主键
    @Column("user_id")
    private Integer userId;
    @Column("user_account")
    private String account;
    @Column("user_password")
    private String password;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
