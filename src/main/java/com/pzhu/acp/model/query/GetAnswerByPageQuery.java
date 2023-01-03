package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:40
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAnswerByPageQuery extends WorkPageQuery implements Serializable {
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
