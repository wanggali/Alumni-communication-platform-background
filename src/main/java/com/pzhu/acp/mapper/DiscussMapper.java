package com.pzhu.acp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.query.GetDiscussByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【discuss】的数据库操作Mapper
* @createDate 2022-11-21 21:34:27
* @Entity com.pzhu.acp.model.Discuss
*/
public interface DiscussMapper extends BaseMapper<Discuss> {

    IPage<DiscussVO> selectDiscussByPageOrParam(Page<DiscussVO> discussPage,@Param("query") GetDiscussByPageQuery getDiscussByPageQuery);
}




