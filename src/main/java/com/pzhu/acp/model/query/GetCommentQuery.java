package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: gali
 * @Date: 2022-11-29 22:55
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetCommentQuery extends WorkPageQuery {
    /**
     * 文章id
     */
    private Long did;

    /**
     * 排序规则
     */
    private Integer orderType;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 地区
     */
    private Long regionId;

    /**
     * 学院
     */
    private Long collegeId;
}
