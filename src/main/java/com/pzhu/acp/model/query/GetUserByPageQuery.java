package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-24 0:26
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserByPageQuery extends WorkPageQuery implements Serializable {
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
