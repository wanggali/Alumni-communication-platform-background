package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.GetPushByPageRequest;
import com.pzhu.acp.model.dto.PushAddRequest;
import com.pzhu.acp.model.dto.PushDeleteRequest;
import com.pzhu.acp.model.dto.PushUpdateRequest;
import com.pzhu.acp.model.entity.Push;
import com.pzhu.acp.model.query.GetPushByPageQuery;
import com.pzhu.acp.service.PushService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-20 19:42
 * @Description:
 */
@RestController
@RequestMapping("/push")
@Slf4j
public class PushController {
    @Resource
    private PushService pushService;

    /**
     * 增加内推
     */
    @PostMapping("/addPush")
    public BaseResponse<Boolean> addPush(@RequestBody PushAddRequest pushAddRequest) {
        if (StringUtils.isBlank(pushAddRequest.getPushUrl()) ||
                StringUtils.isBlank(pushAddRequest.getCompanyName()) ||
                StringUtils.isBlank(pushAddRequest.getCompanyPosition()) ||
                StringUtils.isBlank(pushAddRequest.getCompanyRegion()) ||
                StringUtils.isBlank(pushAddRequest.getCompanySalary()) ||
                StringUtils.isBlank(pushAddRequest.getPositionInfo())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(pushAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (pushAddRequest.getUid() < CommonConstant.MIN_ID ||
                pushAddRequest.getPositionNum() < CommonConstant.POSITION_MIN_NUM) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(pushAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Push push = new Push();
        BeanUtils.copyProperties(pushAddRequest, push);
        Boolean isSuccess = pushService.addPush(push);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 修改职位信息
     */
    @PostMapping("/updatePush")
    public BaseResponse<Boolean> updatePush(@RequestBody PushUpdateRequest pushUpdateRequest) {
        if (StringUtils.isBlank(pushUpdateRequest.getPushUrl()) ||
                StringUtils.isBlank(pushUpdateRequest.getCompanyName()) ||
                StringUtils.isBlank(pushUpdateRequest.getCompanyPosition()) ||
                StringUtils.isBlank(pushUpdateRequest.getCompanyRegion()) ||
                StringUtils.isBlank(pushUpdateRequest.getCompanySalary()) ||
                StringUtils.isBlank(pushUpdateRequest.getPositionInfo())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(pushUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (pushUpdateRequest.getUid() < CommonConstant.MIN_ID ||
                pushUpdateRequest.getId() < CommonConstant.MIN_ID ||
                pushUpdateRequest.getPositionNum() < CommonConstant.POSITION_MIN_NUM) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(pushUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Push push = new Push();
        BeanUtils.copyProperties(pushUpdateRequest, push);
        Boolean isSuccess = pushService.updatePush(push);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除内推
     */
    @DeleteMapping("/deletePush")
    public BaseResponse<Boolean> deletePush(@RequestBody PushDeleteRequest pushDeleteRequest) {
        pushDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = pushService.deletePush(pushDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页显示内推信息
     */
    @PostMapping("/getPushInfoByPageOrParam")
    public BaseResponse<Map<String, Object>> getPushInfoByPageOrParam(@RequestBody GetPushByPageRequest getPushByPageRequest) {
        if (getPushByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getPushByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getPushByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetPushByPageQuery getPushByPageQuery = new GetPushByPageQuery();
        BeanUtils.copyProperties(getPushByPageRequest, getPushByPageQuery);
        Map<String, Object> map = pushService.getPushInfoByPageOrParam(getPushByPageQuery);
        return ResultUtils.success(map);
    }
}
