package org.mirrentools.ost.expression;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;

public class Random {
    static Snowflake snowflake = new Snowflake(1, 1);

    public static String serialId() {
        System.out.println(snowflake.nextId());
        return snowflake.nextIdStr();
    }

    public static String randomNum(int length) {
        return RandomUtil.randomNumbers(length);
    }

    public static String random(int length) {
        return RandomUtil.randomString(length);
    }

    public static void main(String[] args) {
        System.out.println(serialId());
    }
}
