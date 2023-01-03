package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.UserInfo;
import com.pzhu.acp.model.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【user_info】的数据库操作Mapper
 * @createDate 2022-12-23 23:22:07
 * @Entity com.pzhu.acp.model.UserInfo
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfoVO selectUserInfoById(@Param("id") Long id);
}




