package com.pzhu.acp.model.vo;

import com.pzhu.acp.model.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2023-01-02 1:20
 * @Description:
 */
@Data
public class PermissionValueVO implements Serializable {
    /**
     * 菜单id
     */
    private Long permissionId;

    /**
     * 菜单
     */
    private Permission permission;
}