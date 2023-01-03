package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.*;
import com.pzhu.acp.model.entity.Permission;
import com.pzhu.acp.model.entity.RolePermission;
import com.pzhu.acp.model.query.PermissionAddToRoleQuery;
import com.pzhu.acp.model.vo.PermissionVO;
import com.pzhu.acp.model.vo.RolePermissionVO;
import com.pzhu.acp.service.PermissionService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:49
 * @Description:
 */
@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    @PostMapping("/addPermission")
    public BaseResponse<Boolean> addPermission(@RequestBody PermissionAddRequest permissionAddRequest) {
        if (permissionAddRequest.getPid() < CommonConstant.MIN_ID ||
                permissionAddRequest.getType() == null) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(permissionAddRequest.getName()) ||
                StringUtils.isBlank(permissionAddRequest.getPath()) ||
                StringUtils.isBlank(permissionAddRequest.getPermissionValue())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionAddRequest, permission);
        Boolean isSuccess = permissionService.addPermission(permission);
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/updatePermission")
    public BaseResponse<Boolean> updatePermission(@RequestBody PermissionUpdateRequest permissionUpdateRequest) {
        if (permissionUpdateRequest.getPid() < CommonConstant.MIN_ID ||
                permissionUpdateRequest.getId() < CommonConstant.MIN_ID ||
                permissionUpdateRequest.getType() == null) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(permissionUpdateRequest.getName()) ||
                StringUtils.isBlank(permissionUpdateRequest.getPath()) ||
                StringUtils.isBlank(permissionUpdateRequest.getPermissionValue())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionUpdateRequest, permission);
        Boolean isSuccess = permissionService.updatePermission(permission);
        return ResultUtils.success(isSuccess);
    }

    @DeleteMapping("/deletePermission")
    public BaseResponse<Boolean> deletePermission(@RequestBody PermissionDeleteRequest permissionDeleteRequest) {
        permissionDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = permissionService.deletePermission(permissionDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    @GetMapping("/getPermissionByTree")
    public BaseResponse<List<PermissionVO>> getPermissionByTree() {
        List<PermissionVO> list = permissionService.getPermissionByTree();
        return ResultUtils.success(list);
    }

    @PostMapping("/addPermissionToRole")
    public BaseResponse<Boolean> addPermissionToRole(@RequestBody PermissionAddToRoleRequest permissionAddToRoleRequest) {
        if (permissionAddToRoleRequest.getRoleId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionAddToRoleRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (CollectionUtils.isEmpty(permissionAddToRoleRequest.getPermissionId())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(permissionAddToRoleRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PermissionAddToRoleQuery permissionAddToRoleQuery = new PermissionAddToRoleQuery();
        BeanUtils.copyProperties(permissionAddToRoleRequest, permissionAddToRoleQuery);
        permissionService.addPermissionToRole(permissionAddToRoleQuery);
        return ResultUtils.success(Boolean.TRUE);
    }

    @GetMapping("/getRolePermissionInRoleId/{roleId}")
    public BaseResponse<RolePermissionVO> getRolePermissionInRoleId(@PathVariable Long roleId) {
        if (roleId < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(roleId));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RolePermissionVO rolePermissionVO = permissionService.getRolePermissionInRoleId(roleId);
        return ResultUtils.success(rolePermissionVO);
    }
}
