package com.pzhu.acp.enums;

import lombok.Getter;

/**
 * @Auther: gali
 * @Date: 2022-11-28 22:56
 * @Description:
 */
public enum DiscussTypeEnum {
    TOPIC_MESSAGE(0, "帖子"),
    ANSWER_MESSAGE(1, "问答"),
    NEWS_MESSAGE(2, "动态");
    /**
     * 讨论类型
     */
    @Getter
    private final Integer type;

    /**
     * 描述信息
     */
    @Getter
    private final String msg;

    DiscussTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
