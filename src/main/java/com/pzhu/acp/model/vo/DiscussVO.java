package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-07 23:39
 * @Description:
 */
@Data
public class DiscussVO implements Serializable {
    /**
     * 用户信息
     */
    private UserVO userInfo;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String message;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 点赞数
     */
    private Integer up;

    /**
     * 踩次数
     */
    private Integer down;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 创建时间
     */
    private Date createTime;

    private Boolean isUp = Boolean.FALSE;

    private Boolean isDown = Boolean.FALSE;
}
