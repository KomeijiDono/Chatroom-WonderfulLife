package com.edu.seiryo.wonderfulLife.util;

import java.util.List;
import java.util.Map;

/**
 * 简易 JSON 工具类（手动拼接，无第三方依赖）
 * @author KomeijiDono
 * 
 */
public class JsonUtil {

    /** 将 Map 转为 JSON 对象字符串 */
    public static String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("\"").append(escape(entry.getKey())).append("\":");
            sb.append(valueToJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    /** 将对象列表转为 JSON 数组字符串（fields 交替传入 fieldName 和 getter 返回值） */
    public static String toJson(List<?> list, String... fields) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = list.get(i);
            sb.append("{");
            for (int j = 0; j < fields.length; j += 2) {
                if (j > 0) {
                    sb.append(",");
                }
                String field = fields[j];
                String value = fields[j + 1];
                sb.append("\"").append(field).append("\":\"").append(escape(value)).append("\"");
            }
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    /** 将单个值转为 JSON 格式（支持 String/Boolean/Number/null） */
    private static String valueToJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + escape((String) value) + "\"";
        }
        if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        }
        return "\"" + escape(value.toString()) + "\"";
    }

    /** 转义 JSON 字符串中的特殊字符（反斜杠、引号、换行等） */
    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
