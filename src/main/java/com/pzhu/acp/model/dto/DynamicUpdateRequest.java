package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:44
 * @Description:
 */
@Data
public class DynamicUpdateRequest implements Serializable {
    /**
     * id
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
     * 动态内容
     */
    private String content;
}
