package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.constant.OriginConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.GetOriginRequest;
import com.pzhu.acp.model.dto.OriginAddRequest;
import com.pzhu.acp.model.dto.OriginDeleteRequest;
import com.pzhu.acp.model.dto.OriginUpdateRequest;
import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.query.DeleteOriginQuery;
import com.pzhu.acp.model.query.GetOriginQuery;
import com.pzhu.acp.service.OriginService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:29
 * @Description:
 */
@RestController
@RequestMapping("/origin")
@Slf4j
public class OriginController {
    @Resource
    private OriginService originService;

    /**
     * 添加组织
     */
    @PostMapping("/addOrigin")
    public BaseResponse<Boolean> addOrigin(@RequestBody OriginAddRequest originAddRequest) {
        //参数校验
        boolean isErrorParam = originAddRequest.getUid() < CommonConstant.MIN_ID ||
                originAddRequest.getCid() < CommonConstant.MIN_ID ||
                StringUtils.isBlank(originAddRequest.getName()) ||
                originAddRequest.getName().length() > OriginConstant.MAX_ORIGIN_NAME_LENGTH ||
                StringUtils.isBlank(originAddRequest.getAvatar());
        if (isErrorParam) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(originAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Origin origin = new Origin();
        BeanUtils.copyProperties(originAddRequest, origin);
        Boolean flag = originService.addOrigin(origin);
        return ResultUtils.success(flag);
    }

    /**
     * 更新组织
     */
    @PostMapping("/updateOrigin")
    public BaseResponse<Boolean> updateOrigin(@RequestBody OriginUpdateRequest originUpdateRequest) {
        //参数校验
        boolean isErrorParam = originUpdateRequest.getId() < CommonConstant.MIN_ID ||
                originUpdateRequest.getUid() < CommonConstant.MIN_ID ||
                originUpdateRequest.getCid() < CommonConstant.MIN_ID ||
                StringUtils.isBlank(originUpdateRequest.getName()) ||
                originUpdateRequest.getName().length() > OriginConstant.MAX_ORIGIN_NAME_LENGTH ||
                StringUtils.isBlank(originUpdateRequest.getAvatar());
        if (isErrorParam) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(originUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Origin origin = new Origin();
        BeanUtils.copyProperties(originUpdateRequest, origin);
        Boolean flag = originService.updateOrigin(origin);
        return ResultUtils.success(flag);
    }

    /**
     * 删除组织
     */
    @PostMapping("/deleteOrigin")
    public BaseResponse<Boolean> deleteOrigin(@RequestBody OriginDeleteRequest originDeleteRequest) {
        if (CollectionUtils.isEmpty(originDeleteRequest.getIds())) {
            log.error("参数为空，请求失败");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        originDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,删除id为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        DeleteOriginQuery deleteOriginQuery = new DeleteOriginQuery();
        BeanUtils.copyProperties(originDeleteRequest, deleteOriginQuery);
        Boolean flag = originService.deleteOrigin(deleteOriginQuery);
        return ResultUtils.success(flag);
    }

    /**
     * 条件分页获取组织
     */
    @PostMapping("/GetOriginByQueryAndPage")
    public BaseResponse<Map<String, Object>> GetOriginByQueryAndPage(@RequestBody GetOriginRequest getOriginRequest) {
        boolean isErrorParam = getOriginRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getOriginRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE;
        if (isErrorParam) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getOriginRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetOriginQuery getOriginQuery = new GetOriginQuery();
        BeanUtils.copyProperties(getOriginRequest, getOriginQuery);
        Map<String, Object> originVOS = originService.GetOriginByQueryAndPage(getOriginQuery);
        return ResultUtils.success(originVOS);
    }
}
