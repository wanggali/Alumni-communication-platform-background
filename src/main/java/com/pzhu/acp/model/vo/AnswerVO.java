package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-16 17:59
 * @Description:
 */
@Data
public class AnswerVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 被回复人id
     */
    private Long uid;

    /**
     * 被回复人信息
     */
    private UserVO userInfo;

    /**
     * 问题id
     */
    private Long qid;

    /**
     * 回复人id
     */
    private Long answerId;

    /**
     * 回复人信息
     */
    private UserVO answerUserInfo;

    /**
     * 回复内容
     */
    private String answerContent;

    /**
     * 是否采纳（0未采纳，1采纳）
     */
    private Integer isAdopt;

    /**
     * 创建时间
     */
    private Date createTime;
}
