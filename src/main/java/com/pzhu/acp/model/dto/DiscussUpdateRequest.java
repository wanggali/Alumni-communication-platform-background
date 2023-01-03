package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: gali
 * @Date: 2022-11-28 22:51
 * @Description:
 */
@Data
@Setter
@Getter
public class DiscussUpdateRequest {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String message;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;
}
