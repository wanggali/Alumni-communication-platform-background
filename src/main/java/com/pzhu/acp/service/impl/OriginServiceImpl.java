package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.assember.OriginAssembler;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.*;
import com.pzhu.acp.model.entity.*;
import com.pzhu.acp.model.query.DeleteOriginQuery;
import com.pzhu.acp.model.query.GetOriginQuery;
import com.pzhu.acp.model.vo.OriginVO;
import com.pzhu.acp.service.OriginService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【origin】的数据库操作Service实现
 * @createDate 2022-11-13 21:25:08
 */
@Service
@Slf4j
public class OriginServiceImpl extends ServiceImpl<OriginMapper, Origin>
        implements OriginService {

    @Resource
    private OriginMapper originMapper;

    @Resource
    private OriginUserMapper originUserMapper;

    @Resource
    private RegionMapper regionMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CollegeMapper collegeMapper;

    @Override
    @Transactional(rollbackFor = {BusinessException.class, RuntimeException.class})
    public Boolean addOrigin(Origin origin) {
        //查询是否重名
        checkOriginName(origin);
        //检查用户是否存在
        checkUserExisted(origin);
        //检查学院是否存在
        checkCollegeExisted(origin);
        int operationNum = originMapper.insert(origin);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加组织失败");
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        OriginUser originUser = new OriginUser();
        originUser.setOid(origin.getId());
        originUser.setUid(origin.getUid());
        int originUserOperationNum = originUserMapper.insert(originUser);
        if (originUserOperationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加组织用户失败");
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkCollegeExisted(Origin origin) {
        QueryWrapper<College> originQueryWrapper = new QueryWrapper<>();
        originQueryWrapper.eq("id", origin.getCid());
        Long count = collegeMapper.selectCount(originQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该学院不存在，该学院id为：{}", GsonUtil.toJson(origin.getCid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    private void checkUserExisted(Origin origin) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", origin.getUid());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该用户id为：{}", GsonUtil.toJson(origin.getUid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    private void checkOriginName(Origin origin) {
        QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
        originQueryWrapper.eq("name", origin.getName());
        Long count = originMapper.selectCount(originQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("组织名字已重复，该名字为：{}", origin.getName());
            throw new BusinessException(ErrorCode.EXISTED_NAME);
        }
    }

    @Override
    public Boolean updateOrigin(Origin origin) {
        //检查用户是否存在
        checkUserExisted(origin);
        //检查学院是否存在
        checkCollegeExisted(origin);
        int operationNum = originMapper.updateById(origin);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新组织失败");
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = {BusinessException.class, RuntimeException.class})
    public Boolean deleteOrigin(DeleteOriginQuery deleteOriginQuery) {
        deleteOriginQuery.getIds().forEach(id -> {
            //查询该id是否存在
            QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
            originQueryWrapper.eq("id", id);
            Long count = originMapper.selectCount(originQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该组织不存在，组织id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
            }
            int operationNum = originMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除组织失败，组织id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
            QueryWrapper<OriginUser> originUserQueryWrapper = new QueryWrapper<>();
            originUserQueryWrapper.eq("oid", id);
            int deleteOperationNum = originUserMapper.delete(originUserQueryWrapper);
            if (deleteOperationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除组织用户失败，组织id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> GetOriginByQueryAndPage(GetOriginQuery getOriginQuery) {
        QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
        //判断是否需要条件查询
        if (!StringUtils.isBlank(getOriginQuery.getOriginName())) {
            originQueryWrapper.like("name", getOriginQuery.getOriginName());
        }
        if (getOriginQuery.getCollegeId() != null && getOriginQuery.getCollegeId() > CommonConstant.MIN_ID) {
            originQueryWrapper.eq("cid", getOriginQuery.getCollegeId());
        }
        if (!StringUtils.isBlank(getOriginQuery.getUserName())) {
            QueryWrapper<User> userMapperQueryWrapper = new QueryWrapper<>();
            userMapperQueryWrapper.like("username", getOriginQuery.getUserName());
            User user = userMapper.selectOne(userMapperQueryWrapper);
            if (user == null) {
                log.warn("没有该用户，该用户名为：{}", getOriginQuery.getUserName());
                return Maps.newHashMap();
            }
            originQueryWrapper.eq("uid", user.getId());
        }
        Page<Origin> originVOPage = new Page<>(getOriginQuery.getPageNum(), getOriginQuery.getPageSize());
        originMapper.selectPage(originVOPage, originQueryWrapper);
        List<OriginVO> originVOS = originVOPage.getRecords().stream()
                .map(item -> {
                    QueryWrapper<College> wrapper = new QueryWrapper<>();
                    wrapper.eq("id", item.getCid());
                    College college = collegeMapper.selectOne(wrapper);
                    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                    userQueryWrapper.eq("id", item.getUid());
                    User user = userMapper.selectOne(userQueryWrapper);
                    //类型转换：origin-》originVO
                    return OriginAssembler.toOriginVO(item, college.getName(), user.getUsername());
                })
                .collect(Collectors.toList());
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", originVOS);
        map.put("current", originVOPage.getCurrent());
        map.put("pages", originVOPage.getPages());
        map.put("size", originVOPage.getSize());
        map.put("total", originVOPage.getTotal());
        //上一页
        map.put("hasNext", originVOPage.hasNext());
        //下一页
        map.put("hasPrevious", originVOPage.hasPrevious());
        return map;
    }
}




