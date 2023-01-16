package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-07 23:42
 * @Description:
 */
@Data
public class GetDiscussByPageRequest extends WorkPageRequest implements Serializable {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 排序规则（2 点赞最多排序，3 踩最多排序 不传为创建事件排序）
     */
    private Integer sortType;

    /**
     * 审核状态
     */
    private Integer isAuditType;

    /**
     * 标签id
     */
    private Long tid;

}
