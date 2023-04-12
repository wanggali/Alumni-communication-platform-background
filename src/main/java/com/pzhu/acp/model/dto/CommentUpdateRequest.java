package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-29 21:58
 * @Description:
 */
@Data
public class CommentUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private Long uid;
}
