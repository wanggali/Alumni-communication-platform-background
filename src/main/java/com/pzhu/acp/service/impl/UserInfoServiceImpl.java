package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.acp.model.entity.UserInfo;
import com.pzhu.acp.service.UserInfoService;
import com.pzhu.acp.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2022-12-23 23:22:07
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




