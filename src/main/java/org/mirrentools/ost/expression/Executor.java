package org.mirrentools.ost.expression;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Executor {
    static List<String> paramsName = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
    static String returnFlag = "return ";

    static {
        try {
            AviatorEvaluator.addStaticFunctions("random", Random.class);
            AviatorEvaluator.addStaticFunctions("date", Date.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void compile(String cmd) {
        AviatorEvaluator.compile(cmd, true);

    }

    public static String getDataWithExpression(String data) {
        String[] split = data.split("}");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
        }
        int startFindIndex = 0;
        int index = -1;
       /* do {
            index = data.indexOf("${", startFindIndex);
            if(index>0){
            }
        }*/
        return "";
    }

    public static String execute(String expression) {
        expression = expression.trim();
        if (expression.startsWith("${")) {
            expression = expression.substring("${".length());
        }
        if (expression.endsWith("}")) {
            expression = expression.substring(0, expression.length() - 1);
        }
        if (!expression.contains("(") && !expression.contains(")") && !expression.contains(";")) {
            expression = expression + "();";
        }
        if (!expression.endsWith(";")) {
            expression += ";";
        } else if (!expression.contains("(") && !expression.contains(")")) {
            expression = expression.substring(0, expression.length() - 1) + "();";
        }


        Map<String, Object> paramMap = new HashMap<>();
        /*
        固态方法编译+动态参数,可以不需要.
        int startIndex = expression.indexOf("(")+1;
        String paramString = expression.substring(startIndex, expression.length() - 2).trim();
        if (!"".equals(paramString)) {
            String[] params = paramString.split(",");
            for (int i = 0; i < params.length; i++) {
                String paramName = paramsName.get(i);

                if(params[i].trim().startsWith("\"")){
                    paramMap.put(paramsName.get(i), params[i].substring(1,params[i].length()-1));
                }else{
                    paramMap.put(paramsName.get(i), Double.valueOf(params[i]));
                }

                params[i] = paramName;
            }
            expression = expression.replace(paramString, Strings.join(Arrays.asList(params), ','));
        }*/
        return (String) AviatorEvaluator.execute(returnFlag + expression, paramMap, true);
    }

    public static void main(String[] args) {
        Expression compiledExp = AviatorEvaluator.compile("return random.serialId();");
        // Execute with injected variables.
        String result =
                (String) compiledExp.execute();
        System.out.println(result);
    }
}
