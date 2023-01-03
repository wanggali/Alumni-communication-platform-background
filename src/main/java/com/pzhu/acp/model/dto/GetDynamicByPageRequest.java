package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:44
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetDynamicByPageRequest extends WorkPageRequest implements Serializable {
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
