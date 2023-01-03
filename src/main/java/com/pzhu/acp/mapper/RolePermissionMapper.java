package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Permission;
import com.pzhu.acp.model.entity.RolePermission;
import com.pzhu.acp.model.vo.PermissionValueVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【role_permission】的数据库操作Mapper
 * @createDate 2022-12-31 00:01:53
 * @Entity com.pzhu.acp.model.RolePermission
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    PermissionValueVO selectPermission(@Param("roleId") Long roleId);
}




