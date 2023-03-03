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
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120797L;

    /**
     * 用户基本信息
     */
    private User user;

    /**
     * 用户详细信息
     */
    private UserInfo userInfo;
}
