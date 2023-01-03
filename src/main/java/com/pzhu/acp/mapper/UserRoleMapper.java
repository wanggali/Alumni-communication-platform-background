package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.UserRole;
import com.pzhu.acp.model.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【user_role】的数据库操作Mapper
* @createDate 2022-12-31 00:01:34
* @Entity com.pzhu.acp.model.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    RoleVO selectRoleByUser(@Param("userId") Long userId);
}




