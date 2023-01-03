package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 23:20
 * @Description:
 */
@Data
public class TagUpdateRequest implements Serializable {
    /**
     * 标签id
     */
    private Long id;

    /**
     * 标签名字
     */
    private String name;
}
