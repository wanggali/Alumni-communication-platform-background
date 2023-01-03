package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:53
 * @Description:
 */
@Data
public class OriginUserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long oid;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 组织信息
     */
    private OriginToVO originInfo;
    /**
     * 用户信息
     */
    private UserVO userInfo;
}
