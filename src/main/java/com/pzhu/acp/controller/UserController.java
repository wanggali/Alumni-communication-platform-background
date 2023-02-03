package com.pzhu.acp.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.*;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.entity.UserInfo;
import com.pzhu.acp.model.query.GetUserByPageQuery;
import com.pzhu.acp.model.query.UserUpdatePasswordQuery;
import com.pzhu.acp.model.vo.UserVO;
import com.pzhu.acp.service.UserService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户接口
 *
 * @author gali
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest.getCollegeId() < CommonConstant.MIN_ID ||
                userRegisterRequest.getRid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userRegisterRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(userRegisterRequest.getEmail()) ||
                StringUtils.isBlank(userRegisterRequest.getPassword()) ||
                StringUtils.isBlank(userRegisterRequest.getCode())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userRegisterRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userRegisterRequest.getPassword().length() < 8) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userRegisterRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterRequest, user);
        Boolean isSuccess = userService.userRegister(user, userRegisterRequest.getCode());
        return ResultUtils.success(isSuccess);
    }

    @GetMapping("/a'd'd")
    public BaseResponse<String> getEmailCode(@RequestParam String email) {
        userService.getEmailCode(email);
        return ResultUtils.success("success");
    }

    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (StringUtils.isBlank(userLoginRequest.getEmail()) ||
                StringUtils.isBlank(userLoginRequest.getPassword())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userLoginRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        user.setPasssword(userLoginRequest.getPassword());
        BeanUtils.copyProperties(userLoginRequest, user);
        String token = userService.userLogin(user, request);
        return ResultUtils.success(token);
    }

    @PostMapping("/adminLogin")
    public BaseResponse<String> adminLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (StringUtils.isBlank(userLoginRequest.getEmail()) ||
                StringUtils.isBlank(userLoginRequest.getPassword())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userLoginRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userLoginRequest, user);
        user.setPasssword(userLoginRequest.getPassword());
        String token = userService.userAdminLogin(user);
        return ResultUtils.success(token);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        int i = userService.userLogout(request);
        return ResultUtils.success(i);
    }

    @GetMapping("/current")
    @SaCheckLogin
    public BaseResponse<UserVO> getCurrentUser(@RequestParam String token) {
        if (StringUtils.isBlank(token)) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(token));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO user = userService.getCurrentUser(token);
        return ResultUtils.success(user);
    }

    @PostMapping("/updateUser")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.getUser() == null ||
                userUpdateRequest.getUserInfo() == null) {
            log.error("参数校验失败,用户基本信息为：{}，详细信息为：{}", GsonUtil.toJson(userUpdateRequest.getUser()),
                    GsonUtil.toJson(userUpdateRequest.getUserInfo()));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userUpdateRequest.getUser().getUsername() != null && userUpdateRequest.getUser().getUsername().length() > CommonConstant.MAX_USERNAME_LENGTH) {
            log.error("参数校验失败,用户基本信息为：{}，详细信息为：{}", GsonUtil.toJson(userUpdateRequest.getUser()),
                    GsonUtil.toJson(userUpdateRequest.getUserInfo()));
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度不能超过20");
        }
        User user = new User();
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userUpdateRequest.getUser(), user);
        BeanUtils.copyProperties(userUpdateRequest.getUserInfo(), userInfo);
        Boolean isSuccess = userService.updateUser(user, userInfo);
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/getUserByPage")
    public BaseResponse<Map<String, Object>> getUserByPage(@RequestBody GetUserByPageRequest getUserByPageRequest) {
        if (getUserByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getUserByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(getUserByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetUserByPageQuery getUserByPageQuery = new GetUserByPageQuery();
        BeanUtils.copyProperties(getUserByPageRequest, getUserByPageQuery);
        Map<String, Object> map = userService.getUserByPage(getUserByPageQuery);
        return ResultUtils.success(map);
    }

    @DeleteMapping("/deleteUser")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest) {
        userDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = userService.deleteUser(userDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    @PostMapping("/updateUserPassword")
    public BaseResponse<Boolean> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest) {
        if (userUpdatePasswordRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userUpdatePasswordRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(userUpdatePasswordRequest.getOldPassword()) ||
                StringUtils.isBlank(userUpdatePasswordRequest.getNewPassword())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userUpdatePasswordRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userUpdatePasswordRequest.getNewPassword().equals(userUpdatePasswordRequest.getOldPassword())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userUpdatePasswordRequest));
            throw new BusinessException(ErrorCode.SAME_PASSWORD);
        }
        if (userUpdatePasswordRequest.getNewPassword().length() < 8) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(userUpdatePasswordRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        UserUpdatePasswordQuery userUpdatePasswordQuery = new UserUpdatePasswordQuery();
        BeanUtils.copyProperties(userUpdatePasswordRequest, userUpdatePasswordQuery);
        Boolean isSuccess = userService.updateUserPassword(userUpdatePasswordQuery);
        return ResultUtils.success(isSuccess);
    }

}
