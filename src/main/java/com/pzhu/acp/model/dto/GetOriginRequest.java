package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:49
 * @Description:
 */
@Data
public class GetOriginRequest extends WorkPageRequest implements Serializable {
    /**
     * 学院名
     */
    private Long collegeId;

    /**
     * 用户
     */
    private String userName;

    /**
     * 组织名
     */
    private String originName;
}
