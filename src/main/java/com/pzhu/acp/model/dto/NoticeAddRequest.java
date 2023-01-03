package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-21 13:32
 * @Description:
 */
@Data
public class NoticeAddRequest implements Serializable {
    /**
     * 公告内容
     */
    private String content;
}
