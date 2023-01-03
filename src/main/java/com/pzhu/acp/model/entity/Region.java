package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName region
 */
@TableName(value ="region")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Region implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 行政区编码
     */
    private Long code;

    /**
     * 地区
     */
    private String label;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 级别
     */
    private String levelId;

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