package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-30 21:40
 * @Description:
 */
@Data
public class ReplyAddRequest implements Serializable {
    /**
     * 被回复人id
     */
    private Long uid;

    /**
     * 评论id
     */
    private Long cid;

    /**
     * 回复人id
     */
    private Long replyId;

    /**
     * 回复内容
     */
    private String replyContent;
}
