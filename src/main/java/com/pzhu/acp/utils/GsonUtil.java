package com.pzhu.acp.utils;

import com.google.gson.Gson;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Auther: gali
 * @Date: 2022-11-21 22:47
 * @Description:
 */
public class GsonUtil {
    private static final Gson gson = new Gson();

    /**
     * 转为json对象
     *
     * @param param
     * @return
     */
    public static String toJson(Object param) {
        if (param == null) {
            return "";
        }
        return gson.toJson(param);
    }

    /**
     * json转为普通对象
     *
     * @param param
     * @param tClass
     * @return
     */
    public static <T> T fromJson(String param, Class<T> tClass) {
        return gson.fromJson(param, tClass);
    }
}
