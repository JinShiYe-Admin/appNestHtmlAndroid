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

    /**
     * 保存个人信息
     *
     * @param value 要保存的信息
     */
    @JavascriptInterface
    public void setStoragePersonInfo(String value) {
        SharePreferencesUtil util = new SharePreferencesUtil(context, ShellConfig.LOCAL_STORAGE);
        util.putString(ShellConfig.PERSON_INFO, value);
    }

    /**
     * 获取个人信息
     *
     * @return String 个人信息
     */
    @JavascriptInterface
    public String getStoragePersonInfo() {
        SharePreferencesUtil util = new SharePreferencesUtil(context, ShellConfig.LOCAL_STORAGE);
        return util.getString(ShellConfig.PERSON_INFO);
    }
}
