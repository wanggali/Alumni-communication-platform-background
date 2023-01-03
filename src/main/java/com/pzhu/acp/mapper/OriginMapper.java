package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.vo.OriginToVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【origin】的数据库操作Mapper
 * @createDate 2022-11-13 21:25:08
 * @Entity com.pzhu.acp.model/entity.Origin
 */
public interface OriginMapper extends BaseMapper<Origin> {
    OriginToVO selectOriginById(@Param("id") Long id);
}




