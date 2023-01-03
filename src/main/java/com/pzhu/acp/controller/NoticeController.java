package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.GetNoticeByPageRequest;
import com.pzhu.acp.model.dto.NoticeAddRequest;
import com.pzhu.acp.model.dto.NoticeDeleteRequest;
import com.pzhu.acp.model.dto.NoticeUpdateRequest;
import com.pzhu.acp.model.entity.Notice;
import com.pzhu.acp.model.query.GetNoticeByPageQuery;
import com.pzhu.acp.service.NoticeService;
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
@RequestMapping("/notice")
@Slf4j
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    /**
     * 添加公告
     */
    @PostMapping("/addNotice")
    public BaseResponse<Boolean> addNotice(@RequestBody NoticeAddRequest noticeAddRequest) {
        if (StringUtils.isBlank(noticeAddRequest.getContent())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(noticeAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAddRequest, notice);
        Boolean isSuccess = noticeService.addNotice(notice);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更新公告
     */
    @PostMapping("/updateNotice")
    public BaseResponse<Boolean> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        if (noticeUpdateRequest.getId() < CommonConstant.MIN_ID ||
                StringUtils.isBlank(noticeUpdateRequest.getContent())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(noticeUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeUpdateRequest, notice);
        Boolean isSuccess = noticeService.updateNotice(notice);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/deleteNotice")
    public BaseResponse<Boolean> deleteNotice(@RequestBody NoticeDeleteRequest noticeDeleteRequest) {
        noticeDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = noticeService.deleteNotice(noticeDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页查看公告
     */
    @PostMapping("/getNoticeByPage")
    public BaseResponse<Map<String, Object>> getNoticeByPage(@RequestBody GetNoticeByPageRequest getNoticeByPageRequest) {
        if (getNoticeByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getNoticeByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(getNoticeByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetNoticeByPageQuery getNoticeByPageQuery = new GetNoticeByPageQuery();
        BeanUtils.copyProperties(getNoticeByPageRequest, getNoticeByPageQuery);
        Map<String, Object> map = noticeService.getNoticeByPage(getNoticeByPageQuery);
        return ResultUtils.success(map);
    }
}
