package org.mirrentools.ost.handler;

import cn.hutool.core.util.StrUtil;
import org.mirrentools.ost.expression.Executor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserParameter {


    public static Map<String, String> resolve(LinkedHashMap<String, String> params) {
        Map<String, String> result = new HashMap<>();
        params.forEach((key, value) -> {
            if (value.contains("${") && value.contains("}") && value.indexOf("{$") < value.indexOf("}")) {
                result.put(key, resolveExpression(value, result));
            } else {
                result.put(key, value);
            }
        });
        return result;
    }


    public static String resolveExpression(String data, Map<String, String> parameterMap) {
        /*boolean isJson = false;
        if (data.startsWith("{") && data.endsWith("}")) {
            isJson = true;
        }*/
        data = data.trim();
        List<String> split = StrUtil.split(data, "}");

        for (int i = 0; i < split.size(); i++) {
            String str = split.get(i);
            if (i < split.size() - 1) {
                str += "}";
            }
            String value = "";
            int index = str.lastIndexOf("${");
            if (index >= 0) {
                String expression = str.substring(index + "${".length(), str.length() - 1);
                if (expression.contains(".")) {
                    //是表达式,需要解析
                    value = Executor.execute(expression);
                } else {
                    //是变量,需要从userParameterMap取
                    value = parameterMap.get(expression);
                }
                if(value!=null) {
                    str = str.replace("${" + expression + "}", value);
                }
            }
            split.set(i, str);
        }
        String result = String.join("", split);
        if (result.contains("${") && result.contains("}") && result.indexOf("{$") < result.indexOf("}")) {
            return resolveExpression(data, parameterMap);
        }
        return result;
    }

    public static void main(String[] args) {
        String aa = "{\"orderNo\":\"${OrderNo}\",\"ReqDate\":\"${ReqDate}\",\"params\":{\"reqdatetime\":\"${ReqDateTime}\",\"aa\":\"bb\"}}";
        for (String s : StrUtil.split(aa, "}")) {
            System.out.println("("+s+")");
        }

        String bb="aa,";
        System.out.println(StrUtil.split(bb,",").size());

    }
}
