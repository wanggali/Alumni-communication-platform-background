package com.pzhu.acp.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.RegionAddRequest;
import com.pzhu.acp.model.dto.RegionUpdateRequest;
import com.pzhu.acp.model.vo.RegionTreeVO;
import com.pzhu.acp.service.RegionService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-10-01 20:54
 * @Description:
 */
@RestController
@RequestMapping("/region")
@Slf4j
public class RegionController {

    @Resource
    private RegionService regionService;

    /**
     * 分级查询地区列表
     */
    @GetMapping("/getRegionTreeInfo")
    public BaseResponse<List<RegionTreeVO>> getRegionTreeInfo() {
        List<RegionTreeVO> list = regionService.getRegionTreeInfo();
        return ResultUtils.success(list);
    }

    /**
     * 增加地区列表
     */
    @PostMapping("/addRegion")
    public BaseResponse<Boolean> addRegion(@RequestBody RegionAddRequest regionAddRequest) {
        if (regionAddRequest.getParentId() == CommonConstant.MIN_ID ||
                regionAddRequest.getLabel() == null) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(regionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean flag = regionService.addRegion(regionAddRequest);
        return ResultUtils.success(flag);
    }

    /**
     * 删除地区
     */
    @DeleteMapping("/deleteRegion/{id}")
    public BaseResponse<Boolean> deleteRegion(@PathVariable Long id,
                                              @RequestParam("levelId") String levelId) {
        if (id < CommonConstant.MIN_ID) {
            log.error("参数校验失败,删除id为:{}",id);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Boolean flag = regionService.deleteRegion(id, levelId);
        return ResultUtils.success(flag);
    }

    /**
     * 修改地区
     */
    @PostMapping("/updateRegion")
    public BaseResponse<Boolean> updateRegion(@RequestBody RegionUpdateRequest regionUpdateRequest) {
        if (regionUpdateRequest.getId() < CommonConstant.MIN_ID ||
                StringUtils.isBlank(regionUpdateRequest.getLabel())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(regionUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean flag = regionService.updateRegion(regionUpdateRequest);
        return ResultUtils.success(flag);
    }
}
