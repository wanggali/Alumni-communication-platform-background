package com.pzhu.acp.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-21 22:33
 * @Description:
 */
@Data
public class WorkPageQuery implements Serializable {
    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 页码
     */
    private Integer pageSize;
}
