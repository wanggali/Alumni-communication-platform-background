package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-05 22:49
 * @Description:
 */
@Data
public class ReplyVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 回复人id
     */
    private Long replyId;

    /**
     * 回复人信息
     */
    private UserVO replyUserInfo;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 点赞数
     */
    private Integer up;

    /**
     * 创建时间
     */
    private Date createTime;
}
