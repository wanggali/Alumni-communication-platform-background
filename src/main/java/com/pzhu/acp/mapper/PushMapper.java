package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Push;
import com.pzhu.acp.model.query.GetPushByPageQuery;
import com.pzhu.acp.model.vo.PushVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【push】的数据库操作Mapper
 * @createDate 2022-12-20 17:54:36
 * @Entity com.pzhu.acp.model.Push
 */
public interface PushMapper extends BaseMapper<Push> {

    IPage<PushVO> selectPushInfoByPageOrParam(Page<PushVO> page, @Param("query") GetPushByPageQuery getPushByPageQuery);
}




