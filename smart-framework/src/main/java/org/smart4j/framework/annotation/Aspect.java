package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by zhaoshiqiang on 2016/11/23.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}
