package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.query.GetQuestionByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.model.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【question】的数据库操作Mapper
 * @createDate 2022-12-16 15:06:17
 * @Entity com.pzhu.acp.model.Question
 */
public interface QuestionMapper extends BaseMapper<Question> {

    IPage<QuestionVO> selectQuestionByPage(Page<QuestionVO> questionPage, @Param("query") GetQuestionByPageQuery getQuestionByPageQuery);
}




