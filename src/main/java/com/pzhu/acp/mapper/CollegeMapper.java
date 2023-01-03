package com.pzhu.acp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.College;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【college】的数据库操作Mapper
* @createDate 2022-12-05 21:01:14
* @Entity com.pzhu.acp.model.College
*/
public interface CollegeMapper extends BaseMapper<College> {
    String selectCollegeNameById(@Param("id") Long id);
}




