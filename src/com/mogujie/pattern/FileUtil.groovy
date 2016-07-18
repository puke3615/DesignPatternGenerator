package com.mogujie.pattern

import com.intellij.notification.EventLog

/**
 * @author zijiao
 * @version 16/7/15
 * Mark 
 */
class FileUtil {

    static void append(String message) {
        new File('/Users/jiao/Desktop/1.txt').append(message)
    }

}
