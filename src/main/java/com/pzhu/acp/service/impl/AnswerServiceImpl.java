package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.AnswerMapper;
import com.pzhu.acp.mapper.QuestionMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.Answer;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.query.GetAnswerByPageQuery;
import com.pzhu.acp.model.vo.AnswerVO;
import com.pzhu.acp.service.AnswerService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【answer】的数据库操作Service实现
 * @createDate 2022-12-16 15:06:05
 */
@Service
@Slf4j
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer>
        implements AnswerService {
    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Override
    public Map<String, Object> getAnswerByPageOrParam(GetAnswerByPageQuery getAnswerByPageQuery) {
        Page<AnswerVO> page = new Page<>(getAnswerByPageQuery.getPageNum(), getAnswerByPageQuery.getPageSize());
        IPage<AnswerVO> result = answerMapper.selectAnswerByPage(page, getAnswerByPageQuery);
        List<AnswerVO> records = result.getRecords();
        records.forEach(item -> {
            item.setCreateTime(new Date(item.getCreateTime().getTime()));
        });
        if (getAnswerByPageQuery.getAdoptType() != null) {
            records = records.stream().filter(item -> item.getIsAdopt().equals(getAnswerByPageQuery.getAdoptType())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(getAnswerByPageQuery.getUserName())) {
            records = records.stream().filter(item -> item.getUserInfo().getUserName().contains(getAnswerByPageQuery.getUserName())).collect(Collectors.toList());
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", result.getCurrent());
        map.put("pages", result.getPages());
        map.put("size", result.getSize());
        map.put("total", result.getTotal());
        return map;
    }

    @Override
    public Boolean deleteAnswer(List<Long> ids) {
        ids.forEach(id -> {
            Answer answer = new Answer();
            answer.setId(id);
            checkAnswer(answer);
            int operationNum = answerMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除回答失败,该问题参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateAnswer(Answer answer) {
        checkAnswer(answer);
        int operationNum = answerMapper.updateById(answer);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新回答失败,该问题参数为:{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkAnswer(Answer answer) {
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("id", answer.getId());
        Long count = answerMapper.selectCount(answerQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("回答不存在，该参数为：{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean addAnswer(Answer answer) {
        checkUser(answer);
        checkQuestion(answer);
        //一个回答 每个用户只能回答一次
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("uid", answer.getUid());
        answerQueryWrapper.eq("qid", answer.getQid());
        answerQueryWrapper.eq("answer_id", answer.getAnswerId());
        Long count = answerMapper.selectCount(answerQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("该用户已回答了该问题，该参数为：{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.ANSWER_QUESTION_EXISTED);
        }
        int operationNum = answerMapper.insert(answer);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加回答失败,该问题参数为:{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkQuestion(Answer answer) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("id", answer.getQid());
        Long count = questionMapper.selectCount(questionQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("问题不存在，该参数为：{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    private void checkUser(Answer answer) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", answer.getUid());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该参数为：{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        QueryWrapper<User> userMapperQueryWrapper = new QueryWrapper<>();
        userMapperQueryWrapper.eq("id", answer.getAnswerId());
        Long count1 = userMapper.selectCount(userMapperQueryWrapper);
        if (count1 == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该参数为：{}", GsonUtil.toJson(answer));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }
}




