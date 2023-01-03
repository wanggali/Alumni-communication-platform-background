package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * @TableName discuss
 */
@TableName(value = "discuss")
@Data
public class Discuss implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 点赞数
     */
    private Integer up;

    /**
     * 踩次数
     */
    private Integer down;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 逻辑删除（0未删除1删除）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}