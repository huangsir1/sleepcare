package com.sleep.utils;
/**
 * 按钮点击监听类
 * @author magic
 *
 */
public class DiaUtilsBtnSleep {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();   
        if ( time - lastClickTime < 500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}
