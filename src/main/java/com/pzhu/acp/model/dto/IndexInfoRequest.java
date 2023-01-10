package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2023-01-09 21:38
 * @Description:
 */
@Data
public class IndexInfoRequest implements Serializable {
    /**
     * 标签id
     */
    private Long tid;
}
