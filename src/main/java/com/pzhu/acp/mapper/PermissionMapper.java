package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Permission;
import com.pzhu.acp.model.vo.PermissionValueVO;
import com.pzhu.acp.model.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【permission】的数据库操作Mapper
 * @createDate 2022-12-31 00:01:41
 * @Entity com.pzhu.acp.model.Permission
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    Permission selectPermissionValue(@Param("id") Long id);
}




