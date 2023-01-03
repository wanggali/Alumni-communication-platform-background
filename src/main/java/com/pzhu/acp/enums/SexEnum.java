package com.pzhu.acp.enums;

import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.exception.BusinessException;
import lombok.Getter;

import java.util.Objects;

/**
 * @Auther: gali
 * @Date: 2022-12-02 23:45
 * @Description:
 */
public enum SexEnum {
    AUDITING(1, "女"),
    AUDIT_PASS(2, "男");

    /**
     * 性别类型
     */
    @Getter
    private final Integer sex;

    /**
     * 描述信息
     */
    @Getter
    private final String msg;

    SexEnum(Integer sex, String msg) {
        this.sex = sex;
        this.msg = msg;
    }

    public static SexEnum getOrderTypeEnum(Integer sex) {
        for (SexEnum sexEnum : values()) {
            if (Objects.equals(sexEnum.getSex(), sex)) {
                return sexEnum;
            }
        }
        throw new BusinessException(ErrorCode.ERROR_ENUM_PARAM);
    }
}
