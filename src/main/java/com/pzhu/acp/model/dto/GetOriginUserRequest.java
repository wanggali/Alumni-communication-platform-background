package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:49
 * @Description:
 */
@Data
public class GetOriginUserRequest extends WorkPageRequest implements Serializable {
    /**
     * 组织id
     */
    private Long oid;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 地区名字
     */
    private String regionName;
}
