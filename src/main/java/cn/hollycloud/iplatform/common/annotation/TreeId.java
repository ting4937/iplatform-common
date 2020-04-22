package cn.hollycloud.iplatform.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2020-04-20
 * Time: 17:15
 */
@Target({ElementType.FIELD})//参数级别
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeId {

}
