package com.roker.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roker.redis.contant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * json转换工具类
 *
 * @author caden
 * @since 2022-10-23
 *
 */
public class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 所有的日期都统一用yyyy-MM-dd HH:mm:ss格式
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.DEFAULT_DATE_FORMAT));
        // 忽略字符串存在，对象不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转json字符串
     *
     * @param value 需要转换的对象
     * @return json格式字符串
     */
    public static String objToStr (Object value) {
        String result = StringUtils.EMPTY;
        try {
            result = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
        }
        return result;
    }

    /**
     * 字符串转自定义对象
     *
     * @param content 需要转换的字符串
     * @param valueType 需要转换后的类型
     * @param <T> 泛型
     * @return 转换后对象
     */
    public static <T> T strToObj (String content, Class<T> valueType) {
        T result = null;
        try {
            result = objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
        }
        return result;
    }

    /**
     * json字符串转换为List
     *
     * @param content 需要转换的字符串
     * @param valueType 需要转换后的类型
     * @param <T> 泛型
     * @return 转换后的集合
     */
    public static <T> List<T> strToList(String content, Class<T> valueType) {
        List<T> list = new ArrayList<>();
        try {
            list = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (JsonProcessingException ex) {
        }
        return list;
    }
}