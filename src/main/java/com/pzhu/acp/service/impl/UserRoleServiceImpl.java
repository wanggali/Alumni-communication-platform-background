package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.acp.model.entity.UserRole;
import com.pzhu.acp.service.UserRoleService;
import com.pzhu.acp.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2022-12-31 00:01:34
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




