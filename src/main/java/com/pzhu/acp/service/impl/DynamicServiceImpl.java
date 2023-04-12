package com.pzhu.acp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.enums.TagsEnum;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.DynamicMapper;
import com.pzhu.acp.mapper.TagMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.entity.Dynamic;
import com.pzhu.acp.model.entity.Tag;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.query.GetDynamicByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.model.vo.DynamicVO;
import com.pzhu.acp.service.DynamicService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【dynamic】的数据库操作Service实现
 * @createDate 2022-12-19 15:38:27
 */
@Service
@Slf4j
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic>
        implements DynamicService {

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
    public Boolean addDynamic(Dynamic dynamic) {
        checkUserExisted(dynamic);
        checkTagExisted(dynamic);
        int operationNum = dynamicMapper.insert(dynamic);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加动态失败,该动态参数为:{}", GsonUtil.toJson(dynamic));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkTagExisted(Dynamic dynamic) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("id", dynamic.getTid());
        Long count = tagMapper.selectCount(tagQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("标签不存在，该参数为：{}", GsonUtil.toJson(dynamic));
            throw new BusinessException(ErrorCode.NO_EXISTED_TAG);
        }
    }

    private void checkUserExisted(Dynamic dynamic) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", dynamic.getUid());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该参数为：{}", GsonUtil.toJson(dynamic));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    @Override
    public Boolean updateByUp(Dynamic dynamic) {
        //查询该动态是否存在
        checkDynamicExisted(dynamic);
        //获取redis中是否有该回复id对应的点赞数
        String dynamicUp = (String) redisTemplate.opsForValue().get(RedisConstant.DYNAMIC_BASE_UP_KEY + SPLIT_SYMBOL + dynamic.getId());

        if (StringUtils.isBlank(dynamicUp)) {
            log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", dynamic.getId());
            //获取数据库里面的点赞数
            Dynamic oldDiscuss = dynamicMapper.selectById(dynamic.getId());
            Integer oldUpNum = oldDiscuss.getUp();
            Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(),
                    GsonUtil.toJson(dynamic.getUid()));
            if (BooleanUtil.isTrue(isMember)) {
                int newDiscussUp = oldUpNum - 1;
                redisTemplate.opsForValue().set(RedisConstant.DYNAMIC_BASE_UP_KEY + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(newDiscussUp));
                redisTemplate.opsForSet().remove(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(dynamic.getUid()));
            } else {
                Integer up = oldUpNum + 1;
                redisTemplate.opsForValue().set(RedisConstant.DYNAMIC_BASE_UP_KEY + SPLIT_SYMBOL + dynamic.getId(),
                        GsonUtil.toJson(up));
                redisTemplate.opsForSet().add(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(),
                        GsonUtil.toJson(dynamic.getUid()));
            }
        } else {
            Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(),
                    GsonUtil.toJson(dynamic.getUid()));
            if (BooleanUtil.isTrue(isMember)) {
                int newDiscussUp = Integer.parseInt(dynamicUp) - 1;
                redisTemplate.opsForValue().set(RedisConstant.DYNAMIC_BASE_UP_KEY + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(newDiscussUp));
                redisTemplate.opsForSet().remove(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(dynamic.getUid()));
            } else {
                int newDiscussUp = Integer.parseInt(dynamicUp) + 1;
                redisTemplate.opsForValue().set(RedisConstant.DYNAMIC_BASE_UP_KEY + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(newDiscussUp));
                redisTemplate.opsForSet().add(RedisConstant.DYNAMIC_UP_USER_IDS + SPLIT_SYMBOL + dynamic.getId(), GsonUtil.toJson(dynamic.getUid()));
            }
        }
        //定时任务每三分钟统计并写入数据库中
        return Boolean.TRUE;
    }

    private void checkDynamicExisted(Dynamic dynamic) {
        QueryWrapper<Dynamic> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.eq("id", dynamic.getId());
        Long count = dynamicMapper.selectCount(dynamicQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("动态不存在，该参数为：{}", GsonUtil.toJson(dynamic));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean updateDynamic(Dynamic dynamic) {
        checkUserExisted(dynamic);
        checkTagExisted(dynamic);
        checkDynamicExisted(dynamic);
        int operationNum = dynamicMapper.updateById(dynamic);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新动态失败,该动态参数为:{}", GsonUtil.toJson(dynamic));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteDynamic(List<Long> ids) {
        ids.forEach(id -> {
            Dynamic dynamic = new Dynamic();
            dynamic.setId(id);
            checkDynamicExisted(dynamic);
            int operationNum = dynamicMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除动态失败,该动态参数为:{}", GsonUtil.toJson(dynamic));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getDynamicByPageOrParam(GetDynamicByPageQuery getDynamicByPageQuery) {
        Page<DynamicVO> page = new Page<>(getDynamicByPageQuery.getPageNum(), getDynamicByPageQuery.getPageSize());
        IPage<DynamicVO> result = dynamicMapper.selectDynamicByPage(page);
        List<DynamicVO> records = result.getRecords();
        records.forEach(item -> item.setCreateTime(new Date(item.getCreateTime().getTime())));
        if (getDynamicByPageQuery.getSortType() != null) {
            records.sort(Comparator.comparing(DynamicVO::getUp).reversed());
        }
        if (StringUtils.isNotBlank(getDynamicByPageQuery.getContent())) {
            records = records.stream().filter(item -> item.getContent().contains(getDynamicByPageQuery.getContent())).collect(Collectors.toList());
        }
        if (getDynamicByPageQuery.getTid() != null) {
            records = records.stream().filter(item -> item.getTid().equals(getDynamicByPageQuery.getTid())).collect(Collectors.toList());
        }
        if (getDynamicByPageQuery.getUid() != null) {
            records = records.stream().filter(item -> item.getUid().equals(getDynamicByPageQuery.getUid())).collect(Collectors.toList());
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
    public DynamicVO getDynamicById(Long id) {
        DynamicVO dynamicVO = dynamicMapper.selectDynamicById(id);
        dynamicVO.setCreateTime(new Date(dynamicVO.getCreateTime().getTime()));
        return dynamicVO;
    }
}




