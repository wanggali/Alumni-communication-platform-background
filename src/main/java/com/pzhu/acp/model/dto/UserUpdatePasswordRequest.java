package com.pzhu.acp.model.dto;

import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.entity.UserInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author gali
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {

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
