package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:22
 * @Description:
 */
@Data
public class RoleAddRequest implements Serializable {
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色值
     */
    private String roleValue;
}
