package com.pzhu.acp.model.vo;

import com.pzhu.acp.model.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2023-01-02 1:01
 * @Description:
 */
@Data
public class RoleVO implements Serializable {
    /**
     * userId
     */
    private Long userId;

    /**
     * roleId
     */
    private Long roleId;

    /**
     * roleName
     */
    private String roleName;

    /**
     * 菜单值
     */
    private List<PermissionValueVO> permissionVO;
}
