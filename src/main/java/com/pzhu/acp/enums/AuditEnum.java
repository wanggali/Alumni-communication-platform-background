package com.pzhu.acp.enums;

import lombok.Getter;

/**
 * @Auther: gali
 * @Date: 2022-12-02 23:45
 * @Description:
 */
public enum AuditEnum {
    AUDITING(0, "审核中"),
    AUDIT_PASS(1, "审核通过"),
    AUDIT_REFUSE(2, "审核未通过");
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

    AuditEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
