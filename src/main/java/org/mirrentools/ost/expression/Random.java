package org.mirrentools.ost.expression;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;

public class Random {
    static Snowflake snowflake = new Snowflake(1, 1);

    public static String serialId() {
        return snowflake.nextIdStr();
    }

    public static String num(int length) {
        return RandomUtil.randomNumbers(length);
    }

    public static String string(int length) {
        return RandomUtil.randomString(length);
    }

    public static void main(String[] args) {
        System.out.println(serialId());
    }
}
