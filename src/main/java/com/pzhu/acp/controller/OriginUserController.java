package com.pzhu.acp.controller;

import com.pzhu.acp.assember.OriginUserAssembler;
import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.GetOriginUserRequest;
import com.pzhu.acp.model.dto.OriginUserAddRequest;
import com.pzhu.acp.model.dto.OriginUserDeleteRequest;
import com.pzhu.acp.model.entity.OriginUser;
import com.pzhu.acp.model.query.DeleteOriginUserQuery;
import com.pzhu.acp.model.query.GetOriginUserQuery;
import com.pzhu.acp.service.OriginUserService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-11-21 22:13
 * @Description:
 */
@RestController
@RequestMapping("/originUser")
@Slf4j
public class OriginUserController {

    @Resource
    private OriginUserService originUserService;

    /**
     * 增加组织用户
     */
    @PostMapping("/addOriginUser")
    public BaseResponse<Boolean> addOriginUser(@RequestBody OriginUserAddRequest originUserAddRequest) {
        if (originUserAddRequest.getUid() < CommonConstant.MIN_ID ||
                originUserAddRequest.getOid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(originUserAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OriginUser originUser = new OriginUser();
        BeanUtils.copyProperties(originUserAddRequest, originUser);
        Boolean flag = originUserService.addOriginUser(originUser);
        return ResultUtils.success(flag);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/deleteOriginUser")
    public BaseResponse<Boolean> deleteOriginUser(@RequestBody OriginUserDeleteRequest originUserDeleteRequest) {
        DeleteOriginUserQuery deleteOriginUserQuery = OriginUserAssembler.toDeleteOriginUserQuery(originUserDeleteRequest);
        Boolean flag = originUserService.deleteOriginUser(deleteOriginUserQuery);
        return ResultUtils.success(flag);
    }

    /**
     * 分页展示组织下的所有用户
     */
    @PostMapping("/getOriginUsers")
    public BaseResponse<Map<String, Object>> getOriginUsers(@RequestBody GetOriginUserRequest getOriginUserRequest){
        boolean isErrorParam = getOriginUserRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getOriginUserRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE||
                getOriginUserRequest.getOid()<CommonConstant.MIN_ID;
        if (isErrorParam) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getOriginUserRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetOriginUserQuery getOriginUserQuery = new GetOriginUserQuery();
        BeanUtils.copyProperties(getOriginUserRequest,getOriginUserQuery);
        Map<String,Object> map = originUserService.getOriginUsers(getOriginUserQuery);
        return ResultUtils.success(map);
    }
}
