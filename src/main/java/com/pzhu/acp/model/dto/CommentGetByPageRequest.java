package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:58
 * @Description:
 */
@Data
public class CommentGetByPageRequest extends WorkPageRequest implements Serializable {
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
