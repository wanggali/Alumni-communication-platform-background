package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-23 23:58
 * @Description:
 */
@Data
public class UserInfoVO implements Serializable {
    /**
     * 校友id
     */
    private Long id;

    /**
     * 校友性别（1女，2男）
     */
    private Integer sex;

    /**
     * 性别
     */
    private String sexType;

    /**
     * 校友年龄
     */
    private Integer age;

    /**
     * 校友生日
     */
    private Date birthday;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 校友邮箱
     */
    private String email;

    /**
     * 校友qq
     */
    private String qq;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 微博号
     */
    private String microblog;

    /**
     * 地址
     */
    private String address;

    /**
     * 创建时间
     */
    private Date joinTime;
}
