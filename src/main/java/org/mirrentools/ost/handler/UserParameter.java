package org.mirrentools.ost.handler;

import org.mirrentools.ost.expression.Executor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserParameter {
    static Map<String, String> userParameterMap = new HashMap<>();

    public static void resolve(LinkedHashMap<String, String> params) {
        params.forEach((key, value) -> {
            if (value.contains("${") && value.contains("}") && value.indexOf("{$") < value.indexOf("}")) {
                userParameterMap.put(key, resolveExpression(value));
            } else {
                userParameterMap.put(key, value);
            }
        });
    }

    public static String resolveExpression(String data) {
        String[] split = data.split("}");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            String value = "";
            int index = str.lastIndexOf("${");
            if (index >= 0) {
                String expression = str.substring(index + "${".length());
                if (expression.contains(".")) {
                    //是表达式,需要解析
                    value = Executor.execute(expression);
                } else {
                    //是变量,需要从userParameterMap取
                    value = userParameterMap.get(expression);
                }
                str = str.replace("${" + expression, value);
            } else {
                str += "}";
            }
            split[i] = str;
        }
        StringBuilder resultSb = new StringBuilder();
        for (String s : split) {
            resultSb.append(s);
        }
        String result = resultSb.toString();
        if (result.contains("${") && result.contains("}") && result.indexOf("{$") < result.indexOf("}")) {
            return resolveExpression(data);
        }
        return result;
    }
}
