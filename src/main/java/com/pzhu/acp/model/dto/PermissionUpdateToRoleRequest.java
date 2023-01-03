package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2023-01-01 20:32
 * @Description:
 */
@Data
public class PermissionUpdateToRoleRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private List<Long> permissionId;
}
