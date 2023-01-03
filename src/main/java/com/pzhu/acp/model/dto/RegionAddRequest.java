package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-10-03 22:02
 * @Description:
 */
@Data
public class RegionAddRequest implements Serializable {
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
}
