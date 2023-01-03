package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:19
 * @Description:
 */
@Data
public class CollegeUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;

}
