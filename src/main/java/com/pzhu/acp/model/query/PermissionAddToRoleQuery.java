package com.pzhu.acp.model.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2023-01-01 23:40
 * @Description:
 */
@Data
public class PermissionAddToRoleQuery implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private List<Long> permissionId;
}
