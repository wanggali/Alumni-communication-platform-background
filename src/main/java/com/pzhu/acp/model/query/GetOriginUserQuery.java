package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:56
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetOriginUserQuery extends WorkPageQuery implements Serializable {
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
