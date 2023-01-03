package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:53
 * @Description:
 */
@Data
public class OriginVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 学院id
     */
    private Long cid;

    /**
     * 所属学院
     */
    private String collegeName;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 创建人
     */
    private String userName;

    /**
     * 组织名称
     */
    private String originName;

    /**
     * 组织头像
     */
    private String avatar;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private String createTime;
}
