package net.jiaobaowang.wisdomschool.common;

import android.content.Context;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

/**
 * Created by ShangLinMo on 2018/2/1.
 */

public class JsToJava {

    private Context context;

    public JsToJava(Context context) {
        this.context = context;
    }

    /**
     * 获取UUID
     *
     * @return UUID
     */
    @JavascriptInterface // 高版本需要加这个注解才能生效
    public String getUUID() {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取APP ID
     *
     * @return APP ID
     */
    @JavascriptInterface // 高版本需要加这个注解才能生效
    public String getAPPID() {
        return context.getPackageName();
    }

    /**
     * 获取SCHOOL ID
     *
     * @return SCHOOL ID
     */
    @JavascriptInterface // 高版本需要加这个注解才能生效
    public String getSCHOOLID() {
        return ShellConfig.SCHOOL_ID;
    }

}
