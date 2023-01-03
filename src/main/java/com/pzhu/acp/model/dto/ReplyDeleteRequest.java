package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-30 21:40
 * @Description:
 */
@Data
public class ReplyDeleteRequest implements Serializable {
    /**
     * 回复ids
     */
    private List<Long> ids;
}
