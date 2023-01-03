package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-11-21 23:20
 * @Description:
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户地区id
     */
    private Long regionId;

    /**
     * 用户地区名称
     */
    private String regionName;

    /**
     * 学院id
     */
    private Long collegeId;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户签名
     */
    private String userSign;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 用户加入时间
     */
    private Date joinTime;

    /**
     * 用户详细信息
     */
    private UserInfoVO userInfo;
}
