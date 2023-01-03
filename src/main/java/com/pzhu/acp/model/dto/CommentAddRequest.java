package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:58
 * @Description:
 */
@Data
public class CommentAddRequest implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 文章id
     */
    private Long did;

    /**
     * 评论内容
     */
    private String content;
}
