package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.CommentAddRequest;
import com.pzhu.acp.model.dto.CommentDeleteRequest;
import com.pzhu.acp.model.dto.CommentGetByPageRequest;
import com.pzhu.acp.model.dto.CommentUpdateRequest;
import com.pzhu.acp.model.entity.Comment;
import com.pzhu.acp.model.query.GetCommentQuery;
import com.pzhu.acp.service.CommentService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:47
 * @Description:
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 增加一条评论
     */
    @PostMapping("/addComment")
    public BaseResponse<Boolean> addComment(@RequestBody CommentAddRequest commentAddRequest) {
        if (commentAddRequest.getUid() < CommonConstant.MIN_ID ||
                commentAddRequest.getDid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(commentAddRequest.getContent().trim())) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        boolean isSuccess = commentService.addComment(comment);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更新一条评论
     */
    @PostMapping("/updateComment")
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (commentUpdateRequest.getUp() > CommonConstant.UP_NUM ||
                commentUpdateRequest.getUp() < CommonConstant.REDUCE_UP_NUM) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentUpdateRequest, comment);
        boolean isSuccess = commentService.updateComment(comment);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 批量删除评论
     */
    @DeleteMapping("/deleteComment")
    public BaseResponse<Boolean> deleteComment(@RequestBody CommentDeleteRequest commentDeleteRequest) {
        commentDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        boolean isSuccess = commentService.deleteComment(commentDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页查询、按条件显示评论
     */
    @PostMapping("/getCommentByPageOrParam")
    public BaseResponse<Map<String, Object>> getCommentByPageOrParam(@RequestBody CommentGetByPageRequest commentGetByPageRequest) {
        if (commentGetByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                commentGetByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentGetByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (commentGetByPageRequest.getDid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(commentGetByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetCommentQuery getCommentQuery = new GetCommentQuery();
        BeanUtils.copyProperties(commentGetByPageRequest, getCommentQuery);
        Map<String, Object> commentVOS = commentService.getCommentByPageOrParam(getCommentQuery);
        return ResultUtils.success(commentVOS);
    }
}
