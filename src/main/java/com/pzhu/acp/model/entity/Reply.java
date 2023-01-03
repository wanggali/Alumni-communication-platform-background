package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName reply
 */
@TableName(value = "reply")
@Data
public class Reply implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被回复人id
     */
    private Long uid;

    /**
     * 评论id
     */
    private Long cid;

    /**
     * 回复人id
     */
    private Long replyId;

    /**
     * 帖子id
     */
    private Long did;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 点赞数
     */
    private Integer up;

    /**
     * 逻辑删除（0未删除1删除）
     */
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