package com.pzhu.acp.model.vo;

import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-19 19:54
 * @Description:
 */
@Data
public class DynamicVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户信息
     */
    private UserVO userInfo;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer up;

    /**
     * 创建时间
     */
    private Date createTime;

    private Boolean isUp = Boolean.FALSE;

}
