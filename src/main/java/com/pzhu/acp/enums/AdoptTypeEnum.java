package com.pzhu.acp.enums;

import lombok.Getter;

/**
 * @Auther: gali
 * @Date: 2022-11-29 22:21
 * @Description:
 */
public enum AdoptTypeEnum {
    AGREE_ADOPT(1, "已采纳"),
    REFUSE_ADOPT(0, "未采纳");
    /**
     * 采纳类型
     */
    @Getter
    private final Integer type;

    /**
     * 描述信息
     */
    @Getter
    private final String msg;

    AdoptTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
