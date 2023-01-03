package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:22
 * @Description:
 */
@Data
public class RoleUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色值
     */
    private String roleValue;
}
