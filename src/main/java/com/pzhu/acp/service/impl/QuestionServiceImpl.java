package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.TagMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.entity.Tag;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.query.GetQuestionByPageQuery;
import com.pzhu.acp.model.query.WorkPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.model.vo.QuestionVO;
import com.pzhu.acp.service.QuestionService;
import com.pzhu.acp.mapper.QuestionMapper;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【question】的数据库操作Service实现
 * @createDate 2022-12-16 15:06:17
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TagMapper tagMapper;

    @Override
    public Boolean addQuestion(Question question) {
        Long uid = question.getUid();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", uid);
        Long userCount = userMapper.selectCount(userQueryWrapper);
        if (userCount == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该参数为：{}", GsonUtil.toJson(question));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        checkTag(question);
        int operationNum = questionMapper.insert(question);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加问题失败,该问题参数为:{}", GsonUtil.toJson(question));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkTag(Question question) {
        Long tid = question.getTid();
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("id", tid);
        Long tagCount = tagMapper.selectCount(tagQueryWrapper);
        if (tagCount == OperationConstant.COUNT_NUM) {
            log.warn("标签不存在，该参数为：{}", GsonUtil.toJson(question));
            throw new BusinessException(ErrorCode.NO_EXISTED_TAG);
        }
    }

    @Override
    public Boolean updateQuestion(Question question) {
        checkQuest(question.getId());
        checkTag(question);
        int operationNum = questionMapper.updateById(question);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新问题失败,该问题参数为:{}", GsonUtil.toJson(question));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteQuestion(List<Long> ids) {
        ids.forEach(id -> {
            checkQuest(id);
            int operationNum = questionMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除问题失败,该问题参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    private void checkQuest(Long id) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("id", id);
        Long count = questionMapper.selectCount(questionQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该问题不存在，该参数为：{}", GsonUtil.toJson(id));
            throw new BusinessException(ErrorCode.NO_EXISTED_TAG);
        }
    }

    @Override
    public Map<String, Object> getQuestionByPage(GetQuestionByPageQuery workPageQuery) {
        Page<QuestionVO> questionPage = new Page<>(workPageQuery.getPageNum(), workPageQuery.getPageSize());
        IPage<QuestionVO> result = questionMapper.selectQuestionByPage(questionPage, workPageQuery);
        List<QuestionVO> records = result.getRecords();
        records.forEach(item -> {
            item.setCreateTime(new Date(item.getCreateTime().getTime()));
        });
        if (StringUtils.isNotBlank(workPageQuery.getTitle())) {
            records = records.stream().filter(item -> item.getTitle().contains(workPageQuery.getTitle())).collect(Collectors.toList());
        }
        if (workPageQuery.getIsAuditType() != null) {
            records = records.stream().filter(item -> item.getIsAudit().equals(workPageQuery.getIsAuditType())).collect(Collectors.toList());
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", result.getCurrent());
        map.put("pages", result.getPages());
        map.put("size", result.getSize());
        map.put("total", result.getTotal());
        return map;
    }
}




