package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.RoleMapper;
import com.pzhu.acp.mapper.RolePermissionMapper;
import com.pzhu.acp.model.dto.PermissionAddToRoleRequest;
import com.pzhu.acp.model.dto.PermissionUpdateToRoleRequest;
import com.pzhu.acp.model.entity.Permission;
import com.pzhu.acp.model.entity.Region;
import com.pzhu.acp.model.entity.Role;
import com.pzhu.acp.model.entity.RolePermission;
import com.pzhu.acp.model.query.PermissionAddToRoleQuery;
import com.pzhu.acp.model.vo.PermissionVO;
import com.pzhu.acp.model.vo.RegionTreeVO;
import com.pzhu.acp.model.vo.RolePermissionVO;
import com.pzhu.acp.service.PermissionService;
import com.pzhu.acp.mapper.PermissionMapper;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【permission】的数据库操作Service实现
 * @createDate 2022-12-31 00:01:41
 */
@Service
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Boolean addPermission(Permission permission) {
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("path", permission.getPath());
        Long count = permissionMapper.selectCount(permissionQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("菜单路径不存在，该参数为：{}", GsonUtil.toJson(permission));
            throw new BusinessException(ErrorCode.EXISTED_PERMISSION_PATH);
        }
        int operationNum = permissionMapper.insert(permission);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加菜单失败,该菜单参数为:{}", GsonUtil.toJson(permission));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updatePermission(Permission permission) {
        checkPermissionExisted(permission);
        int operationNum = permissionMapper.updateById(permission);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新菜单失败,该问题参数为:{}", GsonUtil.toJson(permission));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkPermissionExisted(Permission permission) {
        Permission permissionData = permissionMapper.selectById(permission.getId());
        if (permissionData == null) {
            log.warn("不存在该菜单,该菜单参数为:{}", GsonUtil.toJson(permission));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deletePermission(List<Long> ids) {
        ids.forEach(id -> {
            Permission permission = new Permission();
            permission.setId(id);
            checkPermissionExisted(permission);
            QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
            permissionQueryWrapper.eq("pid", id);
            Long count = permissionMapper.selectCount(permissionQueryWrapper);
            if (count > OperationConstant.COUNT_NUM) {
                log.warn("该菜单下面还有子菜单，不允许删除，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.EXISTED_PERMISSION_CHILDREN);
            }
            int operationNum = permissionMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除菜单失败,该问题参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public List<PermissionVO> getPermissionByTree() {
        List<Permission> allPermissionList = permissionMapper.selectList(null);
        //递归查询地区，并返回封装类RegionTreeVo
        return buildPermissionTree(allPermissionList);
    }

    @Override
    public Boolean addPermissionToRole(PermissionAddToRoleQuery permissionAddToRoleQuery) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", permissionAddToRoleQuery.getRoleId());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionQueryWrapper);
        List<Long> newPermissionId = rolePermissions.stream()
                .map(RolePermission::getPermissionId).collect(Collectors.toList());
        newPermissionId.stream()
                .filter(item -> !permissionAddToRoleQuery.getPermissionId().contains(item))
                .forEach(item -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(permissionAddToRoleQuery.getRoleId());
                    rolePermission.setPermissionId(item);
                    checkPermissionExisted(rolePermission);
                    checkRoleExisted(rolePermission);
                    QueryWrapper<RolePermission> rolePermissionQueryWrapper1 = new QueryWrapper<>();
                    rolePermissionQueryWrapper1.eq("role_id", rolePermission.getRoleId());
                    rolePermissionQueryWrapper1.eq("permission_id", item);
                    int operationNum = rolePermissionMapper.delete(rolePermissionQueryWrapper1);
                    if (operationNum == OperationConstant.OPERATION_NUM) {
                        log.warn("删除角色对应权限列表失败,该问题参数为:{}", GsonUtil.toJson(rolePermission));
                        throw new BusinessException(ErrorCode.SAVE_ERROR);
                    }
                });
        permissionAddToRoleQuery.getPermissionId().stream()
                .filter(item -> !newPermissionId.contains(item))
                .forEach(item -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(permissionAddToRoleQuery.getRoleId());
                    rolePermission.setPermissionId(item);
                    checkPermissionExisted(rolePermission);
                    checkRoleExisted(rolePermission);
                    int operationNum = rolePermissionMapper.insert(rolePermission);
                    if (operationNum == OperationConstant.OPERATION_NUM) {
                        log.warn("添加角色对应权限列表失败,该问题参数为:{}", GsonUtil.toJson(rolePermission));
                        throw new BusinessException(ErrorCode.SAVE_ERROR);
                    }
                });
        return Boolean.TRUE;
    }

    private void checkRoleExisted(RolePermission permissionAddToRoleRequest) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("id", permissionAddToRoleRequest.getRoleId());
        Long roleCount = roleMapper.selectCount(roleQueryWrapper);
        if (roleCount == OperationConstant.COUNT_NUM) {
            log.warn("没有该角色，该参数为：{}", GsonUtil.toJson(permissionAddToRoleRequest));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    private void checkPermissionExisted(RolePermission permissionAddToRoleRequest) {
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("id", permissionAddToRoleRequest.getPermissionId());
        Long count = permissionMapper.selectCount(permissionQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("没有该权限路径，该参数为：{}", GsonUtil.toJson(permissionAddToRoleRequest));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean updatePermissionToRole(RolePermission rolePermission) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("id", rolePermission.getId());
        Long count = rolePermissionMapper.selectCount(rolePermissionQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("不存在该角色对应的该权限路径，该参数为：{}", GsonUtil.toJson(rolePermission));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        checkPermissionExisted(rolePermission);
        checkRoleExisted(rolePermission);
        int operationNum = rolePermissionMapper.updateById(rolePermission);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新角色对于权限列表失败,该问题参数为:{}", GsonUtil.toJson(rolePermission));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public RolePermissionVO getRolePermissionInRoleId(Long roleId) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionQueryWrapper);
        List<Long> list = Lists.newArrayList();
        RolePermissionVO rolePermissionVO = new RolePermissionVO();
        rolePermissions.forEach(item -> {
            rolePermissionVO.setRoleId(item.getRoleId());
            list.add(item.getPermissionId());
        });
        rolePermissionVO.setPermissionIds(list);
        return rolePermissionVO;
    }

    private List<PermissionVO> buildPermissionTree(List<Permission> allPermissionList) {
        //创建最终封装对象类
        List<PermissionVO> permissionTree = Lists.newArrayList();
        for (Permission permission : allPermissionList) {
            if (0 == permission.getPid()) {
                PermissionVO permissionVO = new PermissionVO();
                permissionVO.setLabel(permission.getName());
                BeanUtils.copyProperties(permission, permissionVO);
                permissionTree.add(selectPermissionTreeList(permissionVO, allPermissionList));
            }
        }
        return permissionTree;
    }

    private PermissionVO selectPermissionTreeList(PermissionVO permissionVO, List<Permission> allPermissionList) {
        //对象初始化
        permissionVO.setChildren(new ArrayList<>());
        for (Permission permission : allPermissionList) {
            if (permission.getPid().equals(permissionVO.getId())) {
                PermissionVO permissionTreeVoChildren = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionTreeVoChildren);
                permissionTreeVoChildren.setLabel(permission.getName());
                //如果children为空，进行初始化操作
                if (permissionVO.getPid() == null) {
                    permissionVO.setChildren(new ArrayList<>());
                }
                permissionVO.getChildren().add(selectPermissionTreeList(permissionTreeVoChildren, allPermissionList));
            }
        }
        return permissionVO;
    }
}




