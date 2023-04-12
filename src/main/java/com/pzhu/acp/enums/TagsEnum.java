package com.pzhu.acp.enums;

import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.exception.BusinessException;
import lombok.Getter;

import java.util.Objects;

/**
 * @Auther: gali
 * @Date: 2023-04-11 23:46
 * @Description:
 */
@Getter
public enum TagsEnum {
    UP("up", "赞"),
    DOWN("down", "踩");

    private final String flag;

    private final String desc;

    TagsEnum(String flag, String desc) {
        this.flag = flag;
        this.desc = desc;
    }

    public static TagsEnum getOrderTypeEnum(String flag) {
        for (TagsEnum sexEnum : values()) {
            if (Objects.equals(sexEnum.getFlag(), flag)) {
                return sexEnum;
            }
        }
        throw new BusinessException(ErrorCode.ERROR_ENUM_PARAM);
    }
}
