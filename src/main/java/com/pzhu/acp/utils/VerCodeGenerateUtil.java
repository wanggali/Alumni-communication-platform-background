package com.pzhu.acp.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @Auther: gali
 * @Date: 2022-12-22 22:18
 * @Description:
 */
public class VerCodeGenerateUtil {
    private static final String SYMBOLS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成六位数的随机数字
     *
     * @return
     */
    public static String generateVerCode() {
        //如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            //生成一个在SYMBOLS长度内的随机数，并把SYMBOLS里对应位置的字符拿出来放进numbers数组里
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        //返会这个个六位随机数
        return new String(numbers);
    }
}