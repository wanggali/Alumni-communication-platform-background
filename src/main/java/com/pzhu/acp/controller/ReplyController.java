package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.ReplyAddRequest;
import com.pzhu.acp.model.dto.ReplyDeleteRequest;
import com.pzhu.acp.model.dto.ReplyUpdateNumRequest;
import com.pzhu.acp.model.entity.Reply;
import com.pzhu.acp.service.ReplyService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:47
 * @Description:
 */
@RestController
@RequestMapping("/reply")
@Slf4j
public class ReplyController {

    @Resource
    private ReplyService replyService;

    /**
     * 增加回复
     */
    @PostMapping("/addReply")
    public BaseResponse<Boolean> addReply(@RequestBody ReplyAddRequest replyAddRequest) {
        if (replyAddRequest.getReplyId() < CommonConstant.MIN_ID ||
                replyAddRequest.getCid() < CommonConstant.MIN_ID ||
                replyAddRequest.getUid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(replyAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyAddRequest, reply);
        boolean isSuccess = replyService.addReply(reply);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 增加点赞数
     */
    @PostMapping("/updateUpNum")
    public BaseResponse<Boolean> updateUpNum(@RequestBody ReplyUpdateNumRequest replyUpdateNumRequest) {
        if (replyUpdateNumRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(replyUpdateNumRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (replyUpdateNumRequest.getUp() > CommonConstant.UP_NUM ||
                replyUpdateNumRequest.getUp() < CommonConstant.REDUCE_UP_NUM) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(replyUpdateNumRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyUpdateNumRequest, reply);
        boolean isSuccess = replyService.updateUpNum(reply);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除回复
     */
    @DeleteMapping("/deleteReply")
    public BaseResponse<Boolean> deleteReply(@RequestBody ReplyDeleteRequest replyDeleteRequest) {
        replyDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败,该id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        boolean isSuccess = replyService.deleteReply(replyDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }
}
