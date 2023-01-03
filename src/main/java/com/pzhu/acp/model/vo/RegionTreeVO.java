package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-10-01 21:01
 * @Description:
 */
@Data
public class RegionTreeVO implements Serializable {
    /**
     * id
     */

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
     * 子类地区
     */
    private List<RegionTreeVO> children;

    private static final long serialVersionUID = 1L;
}
