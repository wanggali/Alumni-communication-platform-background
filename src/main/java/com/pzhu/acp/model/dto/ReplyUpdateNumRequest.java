package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-11-30 21:40
 * @Description:
 */
@Data
public class ReplyUpdateNumRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 点赞数
     */
    private Integer up;
}
