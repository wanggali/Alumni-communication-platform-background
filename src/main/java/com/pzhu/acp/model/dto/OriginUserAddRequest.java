package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:32
 * @Description:
 */
@Data
public class OriginUserAddRequest implements Serializable {
    /**
     * 用户
     */
    private Long uid;

    /**
     * 组织
     */
    private Long oid;
}
