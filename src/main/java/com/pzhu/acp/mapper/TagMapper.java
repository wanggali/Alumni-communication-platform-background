package com.pzhu.acp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Tag;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【tag】的数据库操作Mapper
* @createDate 2022-12-05 23:15:59
* @Entity com.pzhu.acp.model.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {
    String selectTagById(@Param("id") Long id);
}




