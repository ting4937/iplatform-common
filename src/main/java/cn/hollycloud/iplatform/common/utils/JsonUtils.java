package cn.hollycloud.iplatform.common.utils;

import cn.hollycloud.iplatform.common.constant.ValueConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @create: 2018-04-24 17:20
 **/
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper typeMapper = new ObjectMapper();

    static {
        customizeConfig(mapper);
        customizeConfig(typeMapper);
        typeMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        typeMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static void customizeConfig(ObjectMapper mapper) {
        //序列化配置
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); //禁用空对象转换json
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//日期自动转化时间戳
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);//枚举类自动调用toString
        //反序列化配置
        //未知字段或没有getter and setter方法自动过滤
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false);//该特性决定对于json浮点数，是否使用BigDecimal来序列化。如果不允许，则使用Double序列化。
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);//该特性决定对于json整形（非浮点），是否使用BigInteger来序列化。如果不允许，则根据数值大小来确定 是使用Integer}, {@link Long} 或者BigInteger
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);//该特性决定当遇到JSON null的对象是java 原始类型，则是否抛出异常。当false时，则使用0 for 'int', 0.0 for double 来设定原始对象初始值。
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);//该特性决定JSON ARRAY是映射为Object[]还是List<Object>。如果开启，都为Object[]，false时，则使用List。
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);//该特性决定parser是否允许JSON整数以多0开始(比如，如果000001赋值给json某变量，
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);//允许解析注释
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);//是否将允许使用非双引号属性名字
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);//是否允许单引号来包住属性名称和字符串值
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);//是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。
        //LocalDate序列化支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(ValueConstant.DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(Boolean.class, new BooleanDeserializer());
        javaTimeModule.addDeserializer(Integer.class, new IntegerDeserializer());
        mapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        mapper.setDateFormat(new SimpleDateFormat(ValueConstant.DEFAULT_DATE_TIME_FORMAT));
    }

    public static void deseriaNullToEmpty(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new CustomStringDeserializer());
        objectMapper.registerModule(module);
    }

    static class CustomStringDeserializer extends StringDeserializer {
        @Override
        public String getNullValue(DeserializationContext ctxt) throws JsonMappingException {
            return "";
        }
    }

    static class BooleanDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = jp.getCurrentToken();

            if (currentToken.equals(JsonToken.VALUE_STRING)) {
                String text = jp.getText();

                if ("Y".equalsIgnoreCase(text)) {
                    return Boolean.TRUE;
                } else if ("N".equalsIgnoreCase(text)) {
                    return Boolean.FALSE;
                } else if ("0".equals(text)) {
                    return Boolean.FALSE;
                } else if ("1".equals(text)) {
                    return Boolean.TRUE;
                }
                throw ctxt.weirdStringException(text, Boolean.class,
                        "boolean值不正确");
            } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
                return Boolean.FALSE;
                //return null
            } else if (currentToken.equals(JsonToken.VALUE_TRUE)) {
                return Boolean.TRUE;
            } else if (currentToken.equals(JsonToken.VALUE_FALSE)) {
                return Boolean.FALSE;
            } else if (currentToken.equals(JsonToken.VALUE_NUMBER_INT)) {
                int val = jp.getIntValue();
                if (val == 0) {
                    return Boolean.FALSE;
                } else if (val == 1) {
                    return Boolean.TRUE;
                }
                throw ctxt.weirdStringException(val + "", Boolean.class,
                        "boolean值不正确");
            }

            throw ctxt.mappingException("Can't parse boolean value: " + jp.getText());
        }
    }

    static class IntegerDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = jp.getCurrentToken();

            if (currentToken.equals(JsonToken.VALUE_NUMBER_INT)) {
                return jp.getIntValue();
            } else if (currentToken.equals(JsonToken.VALUE_STRING)) {
                if (StringUtils.isEmpty(jp.getText())) {
                    return null;
                }
                return Integer.parseInt(jp.getText());
            } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
                return null;
            }

            throw ctxt.mappingException("Can't parse int value: " + jp.getText());
        }
    }

    public static String serialize(Object obj) {
        return serialize(obj, mapper);
    }

    public static String serialize(Object obj, ObjectMapper mapper) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> tClass, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> tClass) {
        return parse(json, tClass, mapper);
    }

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static ObjectMapper getTypeMapper() {
        return typeMapper;
    }
}
