package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2023-01-02 0:07
 * @Description:
 */
@Data
public class RolePermissionVO implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单ids
     */
    private List<Long> permissionIds;
}
