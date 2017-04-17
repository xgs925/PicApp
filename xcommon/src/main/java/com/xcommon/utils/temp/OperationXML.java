package com.xcommon.utils.temp;

import com.xcommon.utils.invoke.ActivityClassUtil;

import java.lang.reflect.Method;
import java.util.List;

public class OperationXML {
    public static List parserXML(String fileName) throws Exception {
        Class clazz = ActivityClassUtil.getOperationXML();
        Method method = clazz.getMethod("parserXML",String.class);
        return (List) method.invoke(clazz.newInstance(),fileName);
    }

}
