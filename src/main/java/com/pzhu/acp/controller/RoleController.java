package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.*;
import com.pzhu.acp.model.entity.Role;
import com.pzhu.acp.model.entity.UserRole;
import com.pzhu.acp.model.query.GetRoleByPageQuery;
import com.pzhu.acp.model.vo.RoleVO;
import com.pzhu.acp.service.RoleService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:21
 * @Description:
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Resource
    private RoleService roleService;

    @PostMapping("/addRole")
    public BaseResponse<Boolean> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        if (StringUtils.isBlank(roleAddRequest.getRoleName()) ||
                StringUtils.isBlank(roleAddRequest.getRoleValue())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(roleAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleAddRequest, role);
        Boolean isSuccess = roleService.addRole(role);
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/updateRole")
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(roleUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(roleUpdateRequest.getRoleName()) ||
                StringUtils.isBlank(roleUpdateRequest.getRoleValue())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(roleUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateRequest, role);
        Boolean isSuccess = roleService.updateRole(role);
        return ResultUtils.success(isSuccess);
    }

    @DeleteMapping("/deleteRole")
    public BaseResponse<Boolean> deleteRole(@RequestBody RoleDeleteRequest roleDeleteRequest) {
        roleDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = roleService.deleteRole(roleDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/getRoleByPage")
    public BaseResponse<Map<String, Object>> getRoleByPage(@RequestBody GetRoleByPageRequest getRoleByPageRequest) {
        if (getRoleByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getRoleByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(getRoleByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetRoleByPageQuery getRoleByPageQuery = new GetRoleByPageQuery();
        BeanUtils.copyProperties(getRoleByPageRequest, getRoleByPageQuery);
        Map<String, Object> map = roleService.getRoleByPage(getRoleByPageQuery);
        return ResultUtils.success(map);
    }

    @PostMapping("/addRoleToUser")
    public BaseResponse<Boolean> addRoleToUser(@RequestBody RoleAddToUserRequest roleAddToUserRequest) {
        if (roleAddToUserRequest.getRoleId() < CommonConstant.MIN_ID ||
                roleAddToUserRequest.getUserId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(roleAddToUserRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserRole userRole = new UserRole();
        BeanUtils.copyProperties(roleAddToUserRequest, userRole);
        Boolean isSuccess = roleService.addRoleToUser(userRole);
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/updateRoleToUser")
    public BaseResponse<Boolean> updateRoleToUser(@RequestBody RoleUpdateToUserRequest roleUpdateToUserRequest) {
        if (roleUpdateToUserRequest.getRoleId() < CommonConstant.MIN_ID ||
                roleUpdateToUserRequest.getId() < CommonConstant.MIN_ID ||
                roleUpdateToUserRequest.getUserId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(roleUpdateToUserRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserRole userRole = new UserRole();
        BeanUtils.copyProperties(roleUpdateToUserRequest, userRole);
        Boolean isSuccess = roleService.updateRoleToUser(userRole);
        return ResultUtils.success(isSuccess);
    }

    @GetMapping("/getRoleByUser")
    public BaseResponse<RoleVO> getRoleByUser(@RequestParam String token) {
        if (StringUtils.isBlank(token)) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(token));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RoleVO roleVO = roleService.getRoleByUser(token);
        return ResultUtils.success(roleVO);
    }
}
