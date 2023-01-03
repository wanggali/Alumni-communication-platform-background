package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:19
 * @Description:
 */
@Data
public class GetAnswerByPageRequest extends WorkPageRequest implements Serializable {
    /**
     * 问题id
     */
    private Long qid;

    /**
     * 是否采纳
     */
    private Integer adoptType;

    /**
     * 用户名
     */
    private String userName;

}
