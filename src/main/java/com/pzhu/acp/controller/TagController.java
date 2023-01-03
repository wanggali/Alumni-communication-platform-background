package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.TagAddRequest;
import com.pzhu.acp.model.dto.TagDeleteRequest;
import com.pzhu.acp.model.dto.TagUpdateRequest;
import com.pzhu.acp.model.entity.Tag;
import com.pzhu.acp.model.vo.TagVO;
import com.pzhu.acp.service.TagService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-12-05 23:18
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/tag")
public class TagController {
    @Resource
    private TagService tagService;

    /**
     * 添加标签
     */
    @PostMapping("/addTag")
    public BaseResponse<Boolean> addTag(@RequestBody TagAddRequest tagAddRequest) {
        if (StringUtils.isBlank(tagAddRequest.getName().trim())) {
            log.warn("标签名字为空，参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        tag.setName(tagAddRequest.getName());
        Boolean isSuccess = tagService.addTag(tag);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更新标签
     */
    @PostMapping("/updateTag")
    public BaseResponse<Boolean> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest) {
        if (tagUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.warn("标签id参数错误，该id为：{}", GsonUtil.toJson(tagUpdateRequest.getId()));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(tagUpdateRequest.getName().trim())) {
            log.warn("标签名字为空，参数错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        tag.setName(tagUpdateRequest.getName());
        tag.setId(tagUpdateRequest.getId());
        Boolean isSuccess = tagService.updateTag(tag);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/deleteTag")
    public BaseResponse<Boolean> deleteTag(@RequestBody TagDeleteRequest tagDeleteRequest) {
        tagDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.warn("标签id参数错误，该id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = tagService.deleteTag(tagDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 查询所有标签
     */
    @GetMapping("/showAllTag")
    public BaseResponse<List<TagVO>> showAllTag() {
        List<TagVO> tagVOS = tagService.showAllTag();
        return ResultUtils.success(tagVOS);
    }
}
