package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.AnswerAddRequest;
import com.pzhu.acp.model.dto.AnswerDeleteRequest;
import com.pzhu.acp.model.dto.AnswerUpdateRequest;
import com.pzhu.acp.model.dto.GetAnswerByPageRequest;
import com.pzhu.acp.model.entity.Answer;
import com.pzhu.acp.model.query.GetAnswerByPageQuery;
import com.pzhu.acp.service.AnswerService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-16 17:26
 * @Description:
 */
@RestController
@RequestMapping("/answer")
@Slf4j
public class AnswerController {
    @Resource
    private AnswerService answerService;

    /**
     * 增加评论
     */
    @PostMapping("/addAnswer")
    public BaseResponse<Boolean> addAnswer(@RequestBody AnswerAddRequest answerAddRequest) {
        if (answerAddRequest.getAnswerId() < CommonConstant.MIN_ID ||
                answerAddRequest.getUid() < CommonConstant.MIN_ID ||
                answerAddRequest.getQid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(answerAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(answerAddRequest.getAnswerContent())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(answerAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerAddRequest, answer);
        Boolean isSuccess = answerService.addAnswer(answer);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 增加采纳
     */
    @PostMapping("/updateAnswer")
    public BaseResponse<Boolean> updateAnswer(@RequestBody AnswerUpdateRequest answerUpdateRequest) {
        if (answerUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(answerUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerUpdateRequest, answer);
        Boolean isSuccess = answerService.updateAnswer(answer);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除回答
     */
    @DeleteMapping("/deleteAnswer")
    public BaseResponse<Boolean> deleteAnswer(@RequestBody AnswerDeleteRequest answerDeleteRequest) {
        answerDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = answerService.deleteAnswer(answerDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 查询回答信息
     */
    @PostMapping("/getAnswerByPageOrParam")
    public BaseResponse<Map<String, Object>> getAnswerByPageOrParam(@RequestBody GetAnswerByPageRequest getAnswerByPageRequest) {
        if (getAnswerByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getAnswerByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE ||
                getAnswerByPageRequest.getQid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getAnswerByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetAnswerByPageQuery getAnswerByPageQuery = new GetAnswerByPageQuery();
        BeanUtils.copyProperties(getAnswerByPageRequest, getAnswerByPageQuery);
        Map<String, Object> map = answerService.getAnswerByPageOrParam(getAnswerByPageQuery);
        return ResultUtils.success(map);
    }
}
