package com.pzhu.acp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.mapper.UserRoleMapper;
import com.pzhu.acp.model.entity.Role;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.entity.UserRole;
import com.pzhu.acp.model.query.GetRoleByPageQuery;
import com.pzhu.acp.model.vo.RoleVO;
import com.pzhu.acp.service.RoleService;
import com.pzhu.acp.mapper.RoleMapper;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2022-12-31 00:01:45
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean addRole(Role role) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_name", role.getRoleName());
        Long count = roleMapper.selectCount(roleQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("角色已存在，该角色为：{}", GsonUtil.toJson(role));
            throw new BusinessException(ErrorCode.EXISTED_ROLE_NAME);
        }
        int operationNum = roleMapper.insert(role);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增角色失败,该角色为:{}", GsonUtil.toJson(role));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateRole(Role role) {
        checkRoleExisted(role);
        int operationNum = roleMapper.updateById(role);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新角色失败,该角色为:{}", GsonUtil.toJson(role));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkRoleExisted(Role role) {
        Role roleData = roleMapper.selectById(role.getId());
        if (roleData == null) {
            log.warn("角色不存在，该角色为：{}", GsonUtil.toJson(role));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deleteRole(List<Long> ids) {
        ids.forEach(id -> {
            Role role = new Role();
            role.setId(id);
            checkRoleExisted(role);
            int operationNum = roleMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除角色失败,该角色为:{}", GsonUtil.toJson(role));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getRoleByPage(GetRoleByPageQuery getRoleByPageQuery) {
        Page<Role> rolePage = new Page<>(getRoleByPageQuery.getPageNum(), getRoleByPageQuery.getPageSize());
        roleMapper.selectPage(rolePage, null);
        List<Role> records = rolePage.getRecords();
        records.forEach(item -> item.setCreateTime(new Date(item.getCreateTime().getTime())));
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", rolePage.getCurrent());
        map.put("pages", rolePage.getPages());
        map.put("size", rolePage.getSize());
        map.put("total", rolePage.getTotal());
        return map;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, BusinessException.class})
    public Boolean addRoleToUser(UserRole userRole) {
        checkUserExisted(userRole);
        checkRoleExisted(userRole);
        checkUserRoleExisted(userRole);
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userRole.getUserId());
        UserRole userRoleData = userRoleMapper.selectOne(userRoleQueryWrapper);
        if (userRoleData != null) {
            userRole.setId(userRoleData.getId());
            int operationNum = userRoleMapper.updateById(userRole);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("更新用户角色失败,该角色为:{}", GsonUtil.toJson(userRole));
                throw new BusinessException(ErrorCode.UPDATE_ERROR);
            }
        } else {
            int operationNum = userRoleMapper.insert(userRole);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("添加用户角色失败,该角色为:{}", GsonUtil.toJson(userRole));
                throw new BusinessException(ErrorCode.SAVE_ERROR);
            }
        }
        User user = new User();
        user.setId(userRole.getUserId());
        user.setAdminType(Math.toIntExact(userRole.getRoleId()));
        int operationNum = userMapper.updateById(user);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新用户角色失败,该角色为:{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkUserRoleExisted(UserRole userRole) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("role_id", userRole.getRoleId());
        userRoleQueryWrapper.eq("user_id", userRole.getUserId());
        Long count = userRoleMapper.selectCount(userRoleQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("用户角色已存在，该用户角色为：{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.USER_ROLE_IN_SAME);
        }
    }

    private void checkRoleExisted(UserRole userRole) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("id", userRole.getRoleId());
        Long userCount = roleMapper.selectCount(roleQueryWrapper);
        if (userCount == OperationConstant.COUNT_NUM) {
            log.warn("该角色不存在，该参数为：{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    private void checkUserExisted(UserRole userRole) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userRole.getUserId());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该参数为：{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    @Override
    public Boolean updateRoleToUser(UserRole userRole) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("id", userRole.getId());
        Long count = userRoleMapper.selectCount(userRoleQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该用户角色不存在，该参数为：{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        checkUserExisted(userRole);
        checkRoleExisted(userRole);
        int operationNum = userRoleMapper.updateById(userRole);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新用户角色失败,该角色为:{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public RoleVO getRoleByUser(String token) {
        if (StringUtils.isBlank((String) StpUtil.getLoginIdByToken(token))) {
            log.warn("token已过期，请重新登录");
            throw new BusinessException(ErrorCode.TIMEOUT_TOKEN);
        }
        Long userId = Long.parseLong((String) StpUtil.getLoginIdByToken(token));
        return userRoleMapper.selectRoleByUser(userId);
    }
}




