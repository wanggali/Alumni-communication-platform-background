package com.pzhu.acp.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-27 22:53
 * @Description:
 */
@Data
public class UserUpdatePasswordQuery implements Serializable {
    private static final long serialVersionUID = 3191241716373120797L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
