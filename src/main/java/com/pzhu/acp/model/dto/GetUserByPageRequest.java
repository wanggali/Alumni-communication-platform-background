package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-24 0:16
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserByPageRequest extends WorkPageRequest implements Serializable {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 学院id
     */
    private Long collegeId;

    /**
     * 地区id
     */
    private Long regionId;
}
