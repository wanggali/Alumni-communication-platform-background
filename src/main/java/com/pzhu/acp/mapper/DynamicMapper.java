package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Dynamic;
import com.pzhu.acp.model.vo.DynamicVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【dynamic】的数据库操作Mapper
* @createDate 2022-12-19 15:38:27
* @Entity com.pzhu.acp.model.Dynamic
*/
public interface DynamicMapper extends BaseMapper<Dynamic> {

    IPage<DynamicVO> selectDynamicByPage(Page<DynamicVO> page);

    DynamicVO selectDynamicById(@Param("id") Long id);
}




