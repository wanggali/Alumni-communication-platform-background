package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2023-01-01 20:13
 * @Description:
 */
@Data
public class RoleAddToUserRequest implements Serializable {
    /**
     * 权限id
     */
    private Long roleId;

    /**
     * 用户id
     */
    private Long userId;
}
