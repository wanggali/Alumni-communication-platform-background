package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:58
 * @Description:
 */
@Data
public class CommentDeleteRequest implements Serializable {
    /**
     * 评论ids
     */
    private List<Long> ids;
}
