package com.pzhu.acp.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户注册请求体
 *
 * @author gali
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 地区id
     */
    private Long rid;

    /**
     * 学院id
     */
    private Long collegeId;

    /**
     * 验证码
     */
    private String code;
}
