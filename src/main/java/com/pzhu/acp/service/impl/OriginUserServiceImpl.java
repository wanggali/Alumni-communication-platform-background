package com.pzhu.acp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.constant.LockConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.OriginConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.OriginMapper;
import com.pzhu.acp.mapper.OriginUserMapper;
import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.entity.OriginUser;
import com.pzhu.acp.model.query.DeleteOriginUserQuery;
import com.pzhu.acp.model.query.GetOriginUserQuery;
import com.pzhu.acp.model.vo.OriginUserVO;
import com.pzhu.acp.service.OriginUserService;
import com.pzhu.acp.utils.GsonUtil;
import com.pzhu.acp.utils.LockHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【origin_user】的数据库操作Service实现
 * @createDate 2022-11-21 21:34:22
 */
@Service
@Slf4j
public class OriginUserServiceImpl extends ServiceImpl<OriginUserMapper, OriginUser>
        implements OriginUserService {

    @Resource
    private OriginUserMapper originUserMapper;

    @Resource
    private OriginMapper originMapper;

    @Resource
    private LockHelper lockHelper;

    @Override
    public Boolean addOriginUser(OriginUser originUser) {
        //只有一个线程能获取到锁
        lockHelper.tryLock(LockConstant.ORIGIN_BASE_ADD_LOCK + originUser.getId(),
                3000,
                1000,
                TimeUnit.MILLISECONDS, () -> {
                    //判断当前组织人数
                    QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
                    originQueryWrapper.eq("id", originUser.getOid());
                    Long originPeopleNum = originMapper.selectCount(originQueryWrapper);
                    if (originPeopleNum > OriginConstant.MAX_ORIGIN_PEOPLE_NUM) {
                        log.warn("该组织人数已满，组织id为：{}", originUser.getOid());
                        throw new BusinessException(ErrorCode.MORE_ORIGIN_PEOPLE_NUM);
                    }
                    //检查该组织是否拥有用户
                    QueryWrapper<OriginUser> originUserQueryWrapper = new QueryWrapper<>();
                    originUserQueryWrapper.eq("oid", originUser.getOid());
                    originUserQueryWrapper.eq("uid", originUser.getUid());
                    Long count = originUserMapper.selectCount(originUserQueryWrapper);
                    if (count > OperationConstant.COUNT_NUM) {
                        log.warn("该用户已经加入该组织，该用户id为：{}", GsonUtil.toJson(originUser.getUid()));
                        throw new BusinessException(ErrorCode.EXISTED_USER_ORIGIN);
                    }
                    int OperationNum = originUserMapper.insert(originUser);
                    if (OperationNum == OperationConstant.OPERATION_NUM) {
                        log.warn("添加组织用户失败");
                        throw new BusinessException(ErrorCode.SAVE_ERROR);
                    }
                });
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteOriginUser(DeleteOriginUserQuery deleteOriginUserQuery) {
        List<Long> originUserIds = deleteOriginUserQuery.getId();
        if (!CollectionUtils.isEmpty(originUserIds)) {
            //根据id删除用户数据
            originUserIds.forEach(originUserId -> {
                if (originUserId != null) {
                    if (originUserId < CommonConstant.MIN_ID) {
                        log.warn("参数校验失败：{}", GsonUtil.toJson(originUserId));
                        throw new BusinessException(ErrorCode.PARAMS_ERROR);
                    }
                    //查询该用户是否存在该组织中
                    QueryWrapper<OriginUser> originUserQueryWrapper = new QueryWrapper<>();
                    originUserQueryWrapper.eq("id", originUserId);
                    Long count = originUserMapper.selectCount(originUserQueryWrapper);
                    if (count == OperationConstant.COUNT_NUM) {
                        log.warn("该用户不存在该组织，id为：{}", GsonUtil.toJson(originUserId));
                        throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
                    }
                    int operationNum = originUserMapper.deleteById(originUserId);
                    if (operationNum == OperationConstant.OPERATION_NUM) {
                        log.warn("删除组织中的用户失败，id为：{}", GsonUtil.toJson(originUserId));
                        throw new BusinessException(ErrorCode.DELETE_ERROR);
                    }
                }
            });
            return Boolean.TRUE;
        }
        //根据组织id和用户id删除数据
        Long oid = deleteOriginUserQuery.getOid();
        List<Long> uids = deleteOriginUserQuery.getUids();
        if (CollectionUtils.isEmpty(uids)) {
            return Boolean.FALSE;
        }
        uids.forEach(uid -> {
            boolean flag = oid != null && oid > CommonConstant.MIN_ID
                    && uid != null && uid > CommonConstant.MIN_ID;
            if (!flag) {
                log.warn("参数校验失败,组织id为：{}，用户id为：{}", GsonUtil.toJson(oid), GsonUtil.toJson(uid));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            QueryWrapper<OriginUser> originUserQueryWrapper = new QueryWrapper<>();
            originUserQueryWrapper.eq("oid", oid);
            originUserQueryWrapper.eq("uid", uid);
            Long count = originUserMapper.selectCount(originUserQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该用户不存在该组织，id为：{}", GsonUtil.toJson(uid));
                throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
            }
            int operationNum = originUserMapper.delete(originUserQueryWrapper);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除组织中的用户失败，组织id为：{}，用户id为：{}", GsonUtil.toJson(oid), GsonUtil.toJson(uid));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getOriginUsers(GetOriginUserQuery getOriginUserQuery) {
        Page<OriginUserVO> originUserVOPage = new Page<>(getOriginUserQuery.getPageNum(), getOriginUserQuery.getPageSize());
        IPage<OriginUserVO> result = originUserMapper.selectOriginUserVO(originUserVOPage, getOriginUserQuery.getOid());
        List<OriginUserVO> originVOS = result.getRecords();
        if (CollectionUtils.isEmpty(originVOS)) {
            log.info("当前数据为空，id为：{}", GsonUtil.toJson(getOriginUserQuery.getOid()));
            return Maps.newHashMap();
        }
        originVOS.forEach(item -> {
            Date originCreateTime = new Date(item.getCreateTime().getTime());
            Date userCreateTime = new Date(item.getUserInfo().getJoinTime().getTime());
            item.setCreateTime(originCreateTime);
            item.getUserInfo().setJoinTime(userCreateTime);
        });

        originVOS = originVOS.stream().filter(item -> {
            if (!StringUtils.isBlank(getOriginUserQuery.getUserName()) && !StringUtils.isBlank(getOriginUserQuery.getRegionName())) {
                return item.getUserInfo().getRegionName().contains(getOriginUserQuery.getRegionName()) &&
                        item.getUserInfo().getUserName().contains(getOriginUserQuery.getUserName());
            }
            if (!StringUtils.isBlank(getOriginUserQuery.getUserName())) {
                return item.getUserInfo().getUserName().contains(getOriginUserQuery.getUserName());
            }
            if (!StringUtils.isBlank(getOriginUserQuery.getRegionName())) {
                return item.getUserInfo().getRegionName().contains(getOriginUserQuery.getRegionName());
            }
            return true;
        }).collect(Collectors.toList());

        Map<String, Object> map = Maps.newHashMap();
        map.put("items", originVOS);
        map.put("current", result.getCurrent());
        map.put("pages", result.getPages());
        map.put("size", result.getSize());
        map.put("total", result.getTotal());
        return map;
    }
}




