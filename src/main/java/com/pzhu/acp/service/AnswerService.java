package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Answer;
import com.pzhu.acp.model.query.GetAnswerByPageQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【answer】的数据库操作Service
* @createDate 2022-12-16 15:06:05
*/
public interface AnswerService extends IService<Answer> {

    Map<String, Object> getAnswerByPageOrParam(GetAnswerByPageQuery getAnswerByPageQuery);

    Boolean deleteAnswer(List<Long> ids);

    Boolean updateAnswer(Answer answer);

    Boolean addAnswer(Answer answer);
}
