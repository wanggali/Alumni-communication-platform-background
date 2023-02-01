package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.PushMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.Push;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.query.GetPushByPageQuery;
import com.pzhu.acp.model.vo.PushVO;
import com.pzhu.acp.service.PushService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【push】的数据库操作Service实现
 * @createDate 2022-12-20 17:54:36
 */
@Service
@Slf4j
public class PushServiceImpl extends ServiceImpl<PushMapper, Push>
        implements PushService {

    @Resource
    private PushMapper pushMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean addPush(Push push) {
        checkUserExisted(push);
        checkPushInfo(push);
        int operationNum = pushMapper.insert(push);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增内推失败,该内推参数为:{}", GsonUtil.toJson(push));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkPushInfo(Push push) {
        QueryWrapper<Push> pushQueryWrapper = new QueryWrapper<>();
        pushQueryWrapper.eq("company_name", push.getCompanyName());
        pushQueryWrapper.eq("company_position", push.getCompanyPosition());
        pushQueryWrapper.eq("company_region", push.getCompanyRegion());
        pushQueryWrapper.eq("uid", push.getUid());
        Long count = pushMapper.selectCount(pushQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("用户已存在该内推信息，该参数为：{}", GsonUtil.toJson(push));
            throw new BusinessException(ErrorCode.EXISTED_PUSH_INFO);
        }
    }

    private void checkUserExisted(Push push) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", push.getUid());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该参数为：{}", GsonUtil.toJson(push));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    @Override
    public Boolean updatePush(Push push) {
        checkUserExisted(push);
        checkPushExisted(push);
        int operationNum = pushMapper.updateById(push);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新内推失败,该内推参数为:{}", GsonUtil.toJson(push));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkPushExisted(Push push) {
        QueryWrapper<Push> pushQueryWrapper = new QueryWrapper<>();
        pushQueryWrapper.eq("id", push.getId());
        Long count = pushMapper.selectCount(pushQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该内推信息不存在，该参数为：{}", GsonUtil.toJson(push));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deletePush(List<Long> ids) {
        ids.forEach(id -> {
            Push push = new Push();
            push.setId(id);
            checkPushExisted(push);
            int operationNum = pushMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除内推失败,该内推参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getPushInfoByPageOrParam(GetPushByPageQuery getPushByPageQuery) {
        Page<PushVO> page = new Page<>(getPushByPageQuery.getPageNum(), getPushByPageQuery.getPageSize());
        IPage<PushVO> result = pushMapper.selectPushInfoByPageOrParam(page, getPushByPageQuery);
        List<PushVO> records = result.getRecords();
        records.forEach(item -> item.setCreateTime(new Date(item.getCreateTime().getTime())));
        if (StringUtils.isNotBlank(getPushByPageQuery.getCompanyName())) {
            records = records.stream().filter(item -> item.getCompanyName().contains(getPushByPageQuery.getCompanyName())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(getPushByPageQuery.getCompanyRegion())) {
            records = records.stream().filter(item -> item.getCompanyRegion().contains(getPushByPageQuery.getCompanyRegion())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(getPushByPageQuery.getCompanyPosition())) {
            records = records.stream().filter(item -> item.getCompanyPosition().contains(getPushByPageQuery.getCompanyPosition())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(getPushByPageQuery.getCompanySalary())) {
            records = records.stream().filter(item -> {
                BigDecimal income = new BigDecimal(item.getCompanySalary());
                BigDecimal target = new BigDecimal(getPushByPageQuery.getCompanySalary());
                return income.compareTo(target) >= 0;
            }).collect(Collectors.toList());
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




