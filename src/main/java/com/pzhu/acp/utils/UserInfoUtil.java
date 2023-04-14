package com.pzhu.acp.utils;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: gali
 * @Date: 2023-04-13 23:00
 * @Description:
 */
@Slf4j
public class UserInfoUtil {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void setUserId(Long id) {
        tl.set(id);
    }

    public static Long getUserId() {
        if (tl.get() == null) {
            if (StpUtil.isLogin()) {
                setUserId(StpUtil.getLoginIdAsLong());
            } else {
                return null;
            }
        }
        return tl.get();
    }
}
