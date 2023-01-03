package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:38
 * @Description:
 */
@Data
public class OriginUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 所属学院
     */
    private Long cid;

    /**
     * 创建人
     */
    private Long uid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织头像
     */
    private String avatar;
}
