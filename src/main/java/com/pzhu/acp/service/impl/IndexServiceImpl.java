package com.pzhu.acp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.mapper.*;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.entity.Dynamic;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.query.IndexInfoQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.model.vo.DynamicVO;
import com.pzhu.acp.model.vo.QuestionVO;
import com.pzhu.acp.service.IndexService;
import com.pzhu.acp.utils.GsonUtil;
import com.pzhu.acp.utils.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: gali
 * @Date: 2023-01-09 21:36
 * @Description:
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {
    @Resource
    private DiscussMapper discussMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private DynamicMapper dynamicMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 分隔符
     */
    private static final String SPLIT_SYMBOL = ":";

    @Override
    public Map<String, Object> getIndexInfo(IndexInfoQuery indexInfoQuery) {
        QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.eq("is_audit", 1).orderByAsc("create_time").last("limit 3");
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("is_audit", 1).orderByAsc("create_time").last("limit 3");
        QueryWrapper<Dynamic> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.orderByAsc("create_time").last("limit 3");
        if (indexInfoQuery.getTid() != null) {
            discussQueryWrapper.eq("tid", indexInfoQuery.getTid());
            questionQueryWrapper.eq("tid", indexInfoQuery.getTid());
            dynamicQueryWrapper.eq("tid", indexInfoQuery.getTid());
        }
        List<Discuss> discusses = discussMapper.selectList(discussQueryWrapper);
        List<Question> questions = questionMapper.selectList(questionQueryWrapper);
        List<Dynamic> dynamics = dynamicMapper.selectList(dynamicQueryWrapper);
        List<DiscussVO> discussVOS = discusses.stream()
                .map(this::buildDiscussInfo)
                .collect(Collectors.toList());

        List<QuestionVO> questionVOS = questions.stream()
                .map(this::buildQuestionsInfo)
                .collect(Collectors.toList());

        List<DynamicVO> dynamicVOS = dynamics.stream()
                .map(this::buildDynamicInfo)
                .collect(Collectors.toList());

        HashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("discuss", discussVOS);
        map.put("questions", questionVOS);
        map.put("dynamics", dynamicVOS);
        return map;
    }

    private DynamicVO buildDynamicInfo(Dynamic item) {
        DynamicVO dynamicVO = new DynamicVO();
        dynamicVO.setUserInfo(userMapper.selectUserById(item.getUid()));
        dynamicVO.setTagName(tagMapper.selectTagById(item.getTid()));
        item.setCreateTime(new Date(item.getCreateTime().getTime()));
        BeanUtils.copyProperties(item, dynamicVO);
        if (StpUtil.isLogin()) {
            dynamicVO.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + item.getId()));
        }
        return dynamicVO;
    }

    private QuestionVO buildQuestionsInfo(Question item) {
        QuestionVO questionVO = new QuestionVO();
        questionVO.setUserInfo(userMapper.selectUserById(item.getUid()));
        questionVO.setTagName(tagMapper.selectTagById(item.getTid()));
        item.setCreateTime(new Date(item.getCreateTime().getTime()));
        BeanUtils.copyProperties(item, questionVO);
        return questionVO;
    }

    private DiscussVO buildDiscussInfo(Discuss item) {
        DiscussVO discussVO = new DiscussVO();
        discussVO.setUserInfo(userMapper.selectUserById(item.getUid()));
        discussVO.setTagName(tagMapper.selectTagById(item.getTid()));
        item.setCreateTime(new Date(item.getCreateTime().getTime()));
        BeanUtils.copyProperties(item, discussVO);
        if (StpUtil.isLogin()) {
            discussVO.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + item.getId()));
            discussVO.setIsDown(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + item.getId()));
        }
        return discussVO;
    }

    private Boolean checkUserIsUp(Long userId, String redisKey) {
        if (userId != null && redisTemplate.keys(redisKey) != null) {
            return redisTemplate.opsForSet().isMember(redisKey, GsonUtil.toJson(userId));
        }
        return Boolean.FALSE;
    }
}
