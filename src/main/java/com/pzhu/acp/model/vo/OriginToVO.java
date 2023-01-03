package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-22 23:32
 * @Description:
 */
@Data
public class OriginToVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 学院id
     */
    private Long cid;

    /**
     * 组织名称
     */
    private String originName;

    /**
     * 组织头像
     */
    private String originAvatar;

    /**
     * 创建人id
     */
    private Long originCreateId;

    /**
     * 组织人所属学院
     */
    private String originCollegeName;
}
