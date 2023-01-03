package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.dto.PermissionAddToRoleRequest;
import com.pzhu.acp.model.dto.PermissionUpdateToRoleRequest;
import com.pzhu.acp.model.entity.Permission;
import com.pzhu.acp.model.entity.RolePermission;
import com.pzhu.acp.model.query.PermissionAddToRoleQuery;
import com.pzhu.acp.model.vo.PermissionVO;
import com.pzhu.acp.model.vo.RolePermissionVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【permission】的数据库操作Service
* @createDate 2022-12-31 00:01:41
*/
public interface PermissionService extends IService<Permission> {

    Boolean addPermission(Permission permission);

    Boolean updatePermission(Permission permission);

    Boolean deletePermission(List<Long> ids);

    List<PermissionVO> getPermissionByTree();

    Boolean addPermissionToRole(PermissionAddToRoleQuery permissionAddToRoleRequest);

    Boolean updatePermissionToRole(RolePermission permissionUpdateToRoleRequest);

    RolePermissionVO getRolePermissionInRoleId(Long roleId);
}
