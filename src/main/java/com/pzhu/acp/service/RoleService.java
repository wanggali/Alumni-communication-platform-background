package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Role;
import com.pzhu.acp.model.entity.UserRole;
import com.pzhu.acp.model.query.GetRoleByPageQuery;
import com.pzhu.acp.model.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【role】的数据库操作Service
* @createDate 2022-12-31 00:01:45
*/
public interface RoleService extends IService<Role> {

    Boolean addRole(Role role);

    Boolean updateRole(Role role);

    Boolean deleteRole(List<Long> ids);

    Map<String, Object> getRoleByPage(GetRoleByPageQuery getRoleByPageQuery);

    Boolean addRoleToUser(UserRole userRole);

    Boolean updateRoleToUser(UserRole userRole);

    RoleVO getRoleByUser(String token);
}
