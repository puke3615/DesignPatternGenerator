package com.mogujie.pattern.base;

import com.intellij.openapi.ui.Messages;

/**
 * @author zijiao
 * @version 16/5/13
 * @Mark
 */
public class Util {

    public static boolean isEmpty(Object... ss) {
        for (Object s : ss) {
            if (s == null || "".equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static void show(String format, Object... params) {
        String message = isEmpty(format) ? "" : String.format(format, params);
        Messages.showMessageDialog(message, C.Message.TITLE, Messages.getInformationIcon());
    }

}
