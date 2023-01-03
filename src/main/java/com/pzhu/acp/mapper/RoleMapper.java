package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Role;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【role】的数据库操作Mapper
* @createDate 2022-12-31 00:01:45
* @Entity com.pzhu.acp.model.Role
*/
public interface RoleMapper extends BaseMapper<Role> {
    String selectRoleNameByUserId(@Param("roleId") Long roleId);
}




