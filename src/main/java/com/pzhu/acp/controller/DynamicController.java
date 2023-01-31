package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.DynamicAddRequest;
import com.pzhu.acp.model.dto.DynamicDeleteRequest;
import com.pzhu.acp.model.dto.DynamicUpdateRequest;
import com.pzhu.acp.model.dto.GetDynamicByPageRequest;
import com.pzhu.acp.model.entity.Dynamic;
import com.pzhu.acp.model.query.GetDynamicByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.model.vo.DynamicVO;
import com.pzhu.acp.service.DynamicService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:42
 * @Description:
 */
@RestController
@RequestMapping("/dynamic")
@Slf4j
public class DynamicController {
    @Resource
    private DynamicService dynamicService;

    /**
     * 添加动态
     */
    @PostMapping("/addDynamic")
    public BaseResponse<Boolean> addDynamic(@RequestBody DynamicAddRequest dynamicAddRequest) {
        if (dynamicAddRequest.getUid() < CommonConstant.MIN_ID ||
                dynamicAddRequest.getTid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(dynamicAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(dynamicAddRequest.getContent())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(dynamicAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Dynamic dynamic = new Dynamic();
        BeanUtils.copyProperties(dynamicAddRequest, dynamic);
        Boolean isSuccess = dynamicService.addDynamic(dynamic);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更改点赞
     */
    @PostMapping("/updateByUp")
    public BaseResponse<Boolean> updateByUp(@RequestBody DynamicUpdateRequest dynamicUpdateRequest) {
        if (dynamicUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(dynamicUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (dynamicUpdateRequest.getUp() > CommonConstant.UP_NUM ||
                dynamicUpdateRequest.getUp() < CommonConstant.REDUCE_UP_NUM) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(dynamicUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Dynamic dynamic = new Dynamic();
        BeanUtils.copyProperties(dynamicUpdateRequest, dynamic);
        Boolean isSuccess = dynamicService.updateByUp(dynamic);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 修改动态信息
     */
    @PostMapping("/updateDynamic")
    public BaseResponse<Boolean> updateDynamic(@RequestBody DynamicUpdateRequest dynamicUpdateRequest) {
        if (dynamicUpdateRequest.getId() < CommonConstant.MIN_ID ||
                dynamicUpdateRequest.getUid() < CommonConstant.MIN_ID ||
                dynamicUpdateRequest.getTid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(dynamicUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(dynamicUpdateRequest.getContent())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(dynamicUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Dynamic dynamic = new Dynamic();
        BeanUtils.copyProperties(dynamicUpdateRequest, dynamic);
        Boolean isSuccess = dynamicService.updateDynamic(dynamic);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除动态
     */
    @DeleteMapping("/deleteDynamic")
    public BaseResponse<Boolean> deleteDynamic(@RequestBody DynamicDeleteRequest dynamicDeleteRequest) {
        dynamicDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = dynamicService.deleteDynamic(dynamicDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页查询动态
     */
    @PostMapping("/getDynamicByPageOrParam")
    public BaseResponse<Map<String, Object>> getDynamicByPageOrParam(@RequestBody GetDynamicByPageRequest getDynamicByPageRequest) {
        if (getDynamicByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getDynamicByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(getDynamicByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetDynamicByPageQuery getDynamicByPageQuery = new GetDynamicByPageQuery();
        BeanUtils.copyProperties(getDynamicByPageRequest, getDynamicByPageQuery);
        Map<String, Object> map = dynamicService.getDynamicByPageOrParam(getDynamicByPageQuery);
        return ResultUtils.success(map);
    }

    /**
     * 根据id获取讨论详情
     */
    @GetMapping("/getDynamicById/{id}")
    public BaseResponse<DynamicVO> getDynamicById(@PathVariable Long id) {
        if (id == null || id < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(id));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DynamicVO discussInfoById = dynamicService.getDynamicById(id);
        return ResultUtils.success(discussInfoById);
    }

}
