package com.mogujie.pattern.impl.findview;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zijiao
 * @version 16/6/7
 * @Mark
 */
public class NameHook {

    private static final Map<String, String> sClassNameMap = new HashMap<>();

    static {
        sClassNameMap.put("View", "android.view.View");
    }

    public static String getClassName(String className) {
        if (sClassNameMap.containsKey(className)) {
            return sClassNameMap.get(className);
        }
        if (!className.contains(".")) {
            className = "android.widget." + className;
        }
        return className;
    }

    public static String getId(String id) {
        return id;
    }

    public static String getFieldName(String id) {
        id = "m" + id.substring(0, 1).toUpperCase() + id.substring(1);
        return id;
    }

}
