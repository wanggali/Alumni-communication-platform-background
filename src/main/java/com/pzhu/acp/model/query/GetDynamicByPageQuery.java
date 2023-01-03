package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-19 16:00
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetDynamicByPageQuery extends WorkPageQuery implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 排序规则
     */
    private Integer sortType;
}
