package com.pzhu.acp.model.dto;

import lombok.Data;

/**
 * @Auther: gali
 * @Date: 2022-12-16 17:28
 * @Description:
 */
@Data
public class AnswerUpdateRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 是否采纳（0未采纳，1采纳）
     */
    private Integer isAdopt;
}
