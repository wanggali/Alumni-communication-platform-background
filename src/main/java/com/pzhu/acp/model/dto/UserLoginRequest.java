package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author yupi
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;
}
