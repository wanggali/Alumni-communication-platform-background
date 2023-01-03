package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pzhu.acp.mapper.*;
import com.pzhu.acp.model.entity.*;
import com.pzhu.acp.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: gali
 * @Date: 2022-12-28 23:54
 * @Description:
 */
@Service
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private OriginMapper originMapper;

    @Resource
    private CollegeMapper collegeMapper;

    @Resource
    private DiscussMapper discussMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private DynamicMapper dynamicMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Override
    public Map<String, Object> analysisInTotal() {
        Long userCount = userMapper.selectCount(null);
        Long originCount = originMapper.selectCount(null);
        Long collegeCount = collegeMapper.selectCount(null);
        Map<String, Object> map = Maps.newHashMap();
        map.put("user", userCount);
        map.put("origin", originCount);
        map.put("college", collegeCount);
        return map;
    }

    @Override
    public Map<String, Object> analysisInWeek() {
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.add(Calendar.DATE, -7);
        Date start = calendar.getTime();
        QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Dynamic> dynamicQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.between("create_time", start, end);
        Long discussCount = discussMapper.selectCount(discussQueryWrapper);
        dynamicQueryWrapper.between("create_time", start, end);
        Long dynamicCount = dynamicMapper.selectCount(dynamicQueryWrapper);
        questionQueryWrapper.between("create_time", start, end);
        Long questionCount = questionMapper.selectCount(questionQueryWrapper);
        Map<String, Object> map = Maps.newHashMap();
        map.put("discussCount", discussCount);
        map.put("dynamicCount", dynamicCount);
        map.put("questionCount", questionCount);
        return map;
    }

    @Override
    public Map<String, Object> analysisInComment() {
        Map<String, Object> map = Maps.newHashMap();
        List<Long> commentList = Lists.newArrayList();
        List<Long> answerList = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            if (i != 0) {
                calendar.add(Calendar.DATE, -i);
            }
            Date end = calendar.getTime();
            calendar.add(Calendar.DATE, -1);
            Date start = calendar.getTime();
            Long dateCount = getDateCount(start, end);
            Long answerCount = getDateAnswerCount(start, end);
            commentList.add(dateCount);
            answerList.add(answerCount);
        }
        map.put("comment", commentList);
        map.put("answer", answerList);
        return map;
    }

    @Override
    public Map<String, Object> analysisInUp() {
        Map<String, Object> map = Maps.newHashMap();
        List<Long> discussList = Lists.newArrayList();
        List<Long> questionList = Lists.newArrayList();
        List<Long> dynamicList = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            if (i != 0) {
                calendar.add(Calendar.DATE, -i);
            }
            Date end = calendar.getTime();
            calendar.add(Calendar.DATE, -1);
            Date start = calendar.getTime();
            Long discussCount = getDateUpCount(start, end);
            Long questionCount = getDateUpQuestionCount(start, end);
            Long dynamicCount = getDateUpDynamicCount(start, end);
            discussList.add(discussCount);
            questionList.add(questionCount);
            dynamicList.add(dynamicCount);
        }
        map.put("discuss", discussList);
        map.put("question", questionList);
        map.put("dynamic", dynamicList);
        return map;
    }

    private Long getDateUpDynamicCount(Date start, Date end) {
        QueryWrapper<Dynamic> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.between("create_time", start, end);
        return dynamicMapper.selectCount(dynamicQueryWrapper);
    }

    private Long getDateUpQuestionCount(Date start, Date end) {
        QueryWrapper<Question> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.between("create_time", start, end);
        return questionMapper.selectCount(dynamicQueryWrapper);
    }

    private Long getDateUpCount(Date start, Date end) {
        QueryWrapper<Discuss> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.between("create_time", start, end);
        return discussMapper.selectCount(dynamicQueryWrapper);
    }

    private Long getDateAnswerCount(Date start, Date end) {
        QueryWrapper<Answer> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.between("create_time", start, end);
        return answerMapper.selectCount(discussQueryWrapper);
    }

    private Long getDateCount(Date start, Date end) {
        QueryWrapper<Comment> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.between("create_time", start, end);
        return commentMapper.selectCount(discussQueryWrapper);
    }
}
