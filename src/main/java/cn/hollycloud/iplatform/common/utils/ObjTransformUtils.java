package cn.hollycloud.iplatform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-08-01
 * Time: 10:19
 */
public class ObjTransformUtils {
    private final static Logger logger = LoggerFactory.getLogger(ObjTransformUtils.class);

    /*
     * 将父类所有的属性COPY到子类中。
     * 类定义中child一定要extends father；
     * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate
     */
    public static <T> T fatherToChild(Object father, Class<T> childClass) {
        try {
            if (!(childClass.getSuperclass() == father.getClass())) {
                return null;
            }
            T child = childClass.newInstance();
            Class fatherClass = father.getClass();
            Field ff[] = fatherClass.getDeclaredFields();
            for (int i = 0; i < ff.length; i++) {
                Field f = ff[i];//取出每一个属性，如deleteDate
                if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()) || f.isEnumConstant()) {
                    continue;
                }
                f.setAccessible(true);
                Class type = f.getType();
                Method m = fatherClass.getMethod("get" + upperHeadChar(f.getName()));//方法getDeleteDate
                if (m == null) {
                    continue;
                }
                Object obj = m.invoke(father);//取出属性值
                f.set(child, obj);
            }
            return child;
        } catch (Exception e) {
            logger.error("fatherToChild转换异常{}", e);
        }
        return null;
    }

    public static <T> T toOtherObj(Object obj, Class<T> otherClass) {
        String json = JsonUtils.serialize(obj);
        return JsonUtils.parse(json, otherClass);
    }

    public static <T> List<T> toOtherList(List list, Class<T> otherClass) {
        List<T> otherList = new ArrayList<>();
        for (Object obj : list) {
            otherList.add(toOtherObj(obj, otherClass));
        }
        return otherList;
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }
}
