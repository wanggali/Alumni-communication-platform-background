package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-21 22:33
 * @Description:
 */
@Data
public class WorkPageRequest implements Serializable {
    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 页码
     */
    private Integer pageSize;
}
