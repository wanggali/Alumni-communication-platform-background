package com.pzhu.acp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Answer;
import com.pzhu.acp.model.query.GetAnswerByPageQuery;
import com.pzhu.acp.model.vo.AnswerVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【answer】的数据库操作Mapper
* @createDate 2022-12-16 15:06:05
* @Entity com.pzhu.acp.model.Answer
*/
public interface AnswerMapper extends BaseMapper<Answer> {

    IPage<AnswerVO> selectAnswerByPage(Page<AnswerVO> page, @Param("query") GetAnswerByPageQuery getAnswerByPageQuery);
}




