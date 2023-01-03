package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * @TableName origin
 */
@TableName(value = "origin")
@Data
@ToString
public class Origin implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属学院
     */
    private Long cid;

    /**
     * 创建人
     */
    private Long uid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织头像
     */
    private String avatar;

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