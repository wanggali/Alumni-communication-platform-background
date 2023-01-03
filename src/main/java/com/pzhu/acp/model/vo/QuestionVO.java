package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-16 15:41
 * @Description:
 */
@Data
public class QuestionVO implements Serializable {
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
     * 问题标题
     */
    private String title;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 创建时间
     */
    private Date createTime;
}
