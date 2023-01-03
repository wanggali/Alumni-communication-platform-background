package com.pzhu.acp.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-29 22:44
 * @Description:
 */
@Data
public class CommentVO implements Serializable {
    /**
     * id
     */
    private Long id;

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

    /**
     * 点赞数
     */
    private Integer up;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 用户信息
     */
    private UserVO userInfo;

    /**
     * 回复信息
     */
    private List<ReplyVO> replyInfo;
}
