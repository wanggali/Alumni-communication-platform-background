package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:19
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetQuestionByPageRequest extends WorkPageRequest implements Serializable {

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 标题
     */
    private String title;

    /**
     * 审批状态
     */
    private Integer isAuditType;
}
