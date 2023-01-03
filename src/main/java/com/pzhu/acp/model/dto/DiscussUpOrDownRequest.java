package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-28 22:51
 * @Description:
 */
@Data
@Setter
@Getter
public class DiscussUpOrDownRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /*
     * 点赞数
     */
    private Integer up;

    /**
     * 踩数
     */
    private Integer down;
}
