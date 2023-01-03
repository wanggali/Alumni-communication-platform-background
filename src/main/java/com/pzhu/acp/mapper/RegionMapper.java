package com.pzhu.acp.mapper;

import com.pzhu.acp.model.entity.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【region】的数据库操作Mapper
* @createDate 2022-10-01 20:50:00
* @Entity com.pzhu.acp.model.entity.Region
*/
public interface RegionMapper extends BaseMapper<Region> {
    String selectRegionById(@Param("id") Long id);
}




