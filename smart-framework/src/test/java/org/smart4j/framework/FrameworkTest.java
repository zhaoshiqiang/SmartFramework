package org.smart4j.framework;

import com.sun.tools.javac.util.Assert;
import org.junit.Test;
import org.smart4j.framework.util.ClassUtil;

import java.util.Set;

/**
 * Created by zhaoshiqiang on 2016/11/5.
 */
public class FrameworkTest {

    @Test
    public void classUtilTest() throws Exception {
        Set<Class<?>> classSet = ClassUtil.getClassSet("org.smart4j.framework");
        System.out.println(classSet.size());

    }
}
