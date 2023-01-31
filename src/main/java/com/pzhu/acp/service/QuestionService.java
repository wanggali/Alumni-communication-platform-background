package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.query.GetQuestionByPageQuery;
import com.pzhu.acp.model.query.WorkPageQuery;
import com.pzhu.acp.model.vo.QuestionVO;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【question】的数据库操作Service
* @createDate 2022-12-16 15:06:17
*/
public interface QuestionService extends IService<Question> {

    Boolean addQuestion(Question question);

    Boolean updateQuestion(Question question);

    Boolean deleteQuestion(List<Long> ids);

    Map<String, Object> getQuestionByPage(GetQuestionByPageQuery workPageQuery);

    QuestionVO getQuestionInfoById(Long id);
}
