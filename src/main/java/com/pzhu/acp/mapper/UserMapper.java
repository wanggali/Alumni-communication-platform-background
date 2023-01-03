package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.query.GetUserByPageQuery;
import com.pzhu.acp.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 */
public interface UserMapper extends BaseMapper<User> {
    UserVO selectUserById(@Param("id") Long id);

    IPage<UserVO> selectUserByPage(Page<UserVO> page, @Param("query") GetUserByPageQuery getUserByPageQuery);
}




