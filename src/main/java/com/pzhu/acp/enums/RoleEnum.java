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
public enum RoleEnum {
    STUDENT(3L, "校友"),
    COLLEGE_ADMIN(2L, "学院管理员"),
    SCHOOL_ADMIN(2L, "学校管理员");

    /**
     * 角色类型
     */
    @Getter
    private final Long roleId;

    /**
     * 描述信息
     */
    @Getter
    private final String msg;

    RoleEnum(Long roleId, String msg) {
        this.roleId = roleId;
        this.msg = msg;
    }

    public static RoleEnum getOrderTypeEnum(Integer sex) {
        for (RoleEnum roleEnum : values()) {
            if (Objects.equals(roleEnum.getRoleId(), sex)) {
                return roleEnum;
            }
        }
        throw new BusinessException(ErrorCode.ERROR_ENUM_PARAM);
    }
}
