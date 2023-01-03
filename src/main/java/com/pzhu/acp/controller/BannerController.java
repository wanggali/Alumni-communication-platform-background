package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.BannerAddRequest;
import com.pzhu.acp.model.dto.BannerDeleteRequest;
import com.pzhu.acp.model.dto.BannerUpdateRequest;
import com.pzhu.acp.model.dto.GetBannerByPageRequest;
import com.pzhu.acp.model.entity.Banner;
import com.pzhu.acp.model.query.GetBannerByPageQuery;
import com.pzhu.acp.service.BannerService;
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
@RequestMapping("/banner")
@Slf4j
public class BannerController {
    @Resource
    private BannerService bannerService;

    /**
     * 添加轮播图
     */
    @PostMapping("/addBanner")
    public BaseResponse<Boolean> addBanner(@RequestBody BannerAddRequest bannerAddRequest) {
        if (StringUtils.isBlank(bannerAddRequest.getImageUrl()) ||
                StringUtils.isBlank(bannerAddRequest.getName())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(bannerAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerAddRequest, banner);
        Boolean isSuccess = bannerService.addBanner(banner);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 修改轮播图
     */
    @PostMapping("/updateBanner")
    public BaseResponse<Boolean> updateBanner(@RequestBody BannerUpdateRequest bannerUpdateRequest) {
        if (StringUtils.isBlank(bannerUpdateRequest.getImageUrl()) ||
                StringUtils.isBlank(bannerUpdateRequest.getName())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(bannerUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (bannerUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(bannerUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerUpdateRequest, banner);
        Boolean isSuccess = bannerService.updateBanner(banner);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/deleteBanner")
    public BaseResponse<Boolean> deleteBanner(@RequestBody BannerDeleteRequest bannerDeleteRequest) {
        bannerDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = bannerService.deleteBanner(bannerDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页查询轮播图
     */
    @PostMapping("/getBannerByPage")
    public BaseResponse<Map<String, Object>> getBannerByPage(@RequestBody GetBannerByPageRequest getBannerByPageRequest) {
        if (getBannerByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getBannerByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getBannerByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetBannerByPageQuery getBannerByPageQuery = new GetBannerByPageQuery();
        BeanUtils.copyProperties(getBannerByPageRequest, getBannerByPageQuery);
        Map<String, Object> map = bannerService.getBannerByPage(getBannerByPageQuery);
        return ResultUtils.success(map);
    }
}
