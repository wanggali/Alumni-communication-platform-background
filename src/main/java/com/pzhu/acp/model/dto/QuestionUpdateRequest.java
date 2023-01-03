package com.pzhu.acp.model.dto;

import lombok.Data;

/**
 * @Auther: gali
 * @Date: 2022-12-16 15:11
 * @Description:
 */
@Data
public class QuestionUpdateRequest {
    /**
     * id
     */
    private Long id;


    /**
     * 标签id
     */
    private Long tid;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;
}
