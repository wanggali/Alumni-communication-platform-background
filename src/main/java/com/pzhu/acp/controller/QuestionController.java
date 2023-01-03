package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.enums.AuditEnum;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.GetQuestionByPageRequest;
import com.pzhu.acp.model.dto.QuestionAddRequest;
import com.pzhu.acp.model.dto.QuestionDeleteRequest;
import com.pzhu.acp.model.dto.QuestionUpdateRequest;
import com.pzhu.acp.model.entity.Question;
import com.pzhu.acp.model.query.GetQuestionByPageQuery;
import com.pzhu.acp.service.QuestionService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-16 15:09
 * @Description:
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    @Resource
    private QuestionService questionService;

    /**
     * 添加问题
     */
    @PostMapping("/addQuestion")
    public BaseResponse<Boolean> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        if (questionAddRequest.getUid() == null || questionAddRequest.getTid() == null) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(questionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(questionAddRequest.getTitle()) || StringUtils.isBlank(questionAddRequest.getContent())) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(questionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (questionAddRequest.getTitle().length() > CommonConstant.TITLE_MAX_LENGTH ||
                questionAddRequest.getContent().length() < CommonConstant.CONTENT_MIN_LENGTH) {
            System.out.println(questionAddRequest.getTitle().length() > CommonConstant.TITLE_MAX_LENGTH);
            System.out.println(questionAddRequest.getContent().length() < CommonConstant.CONTENT_MIN_LENGTH);
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(questionAddRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        Boolean isSuccess = questionService.addQuestion(question);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更新问题及审核
     */
    @PostMapping("/updateQuestion")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest.getId() < CommonConstant.MIN_ID ||
                questionUpdateRequest.getTid() < CommonConstant.MIN_ID) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(questionUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (questionUpdateRequest.getIsAudit() < AuditEnum.AUDITING.getType() ||
                questionUpdateRequest.getIsAudit() > AuditEnum.AUDIT_REFUSE.getType()) {
            log.error("参数校验失败,该参数为：{}", GsonUtil.toJson(questionUpdateRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        Boolean isSuccess = questionService.updateQuestion(question);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除问题
     */
    @DeleteMapping("/deleteQuestion")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody QuestionDeleteRequest questionDeleteRequest) {
        questionDeleteRequest.getIds().forEach(item -> {
            if (item < CommonConstant.MIN_ID) {
                log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(item));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = questionService.deleteQuestion(questionDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页查询问题
     */
    @PostMapping("/getQuestionByPage")
    public BaseResponse<Map<String, Object>> getQuestionByPage(@RequestBody GetQuestionByPageRequest getQuestionByPageRequest) {
        if (getQuestionByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getQuestionByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.error("参数校验失败，该参数为：{}", GsonUtil.toJson(getQuestionByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetQuestionByPageQuery workPageQuery = new GetQuestionByPageQuery();
        BeanUtils.copyProperties(getQuestionByPageRequest, workPageQuery);
        Map<String, Object> map = questionService.getQuestionByPage(workPageQuery);
        return ResultUtils.success(map);
    }
}
