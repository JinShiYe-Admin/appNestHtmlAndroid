package net.jiaobaowang.wisdomschool.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import net.jiaobaowang.wisdomschool.shell_interface.FileChooser;

/**
 * 网页中选取文件
 * Created by ShangLinMo on 2018/2/1.
 */

public class ShellWebChromeClient extends WebChromeClient {
    private static final String TAG = "ShellWebChromeClient";
    private Context context;
    private FileChooser fileChooser;

    public ShellWebChromeClient(Context context, FileChooser fileChooser) {
        this.context = context;
        this.fileChooser = fileChooser;
    }

    // android < 3.0
    protected void openFileChooser(ValueCallback<Uri> valueCallback) {
        Log.i(TAG, "openFileChooser:0");
        fileChooser.lowVersion(valueCallback);
        lowFileChooser("", "");
    }

    // android >= 3.0
    protected void openFileChooser(ValueCallback valueCallback, String acceptType) {
        Log.i(TAG, "openFileChooser:1 acceptType:" + acceptType);
        fileChooser.lowVersion(valueCallback);
        lowFileChooser(acceptType, "");
    }

    //android >= 4.1
    protected void openFileChooser(ValueCallback<Uri> valueCallback, String accept, String capture) {
        Log.i(TAG, "openFileChooser:2 acceptType:" + accept + " capture:" + capture);
        fileChooser.lowVersion(valueCallback);
        lowFileChooser(accept, capture);
    }

    // android >= 5.0
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        Log.i(TAG, "onShowFileChooser:0");
        Intent i = fileChooserParams.createIntent();
        try {
            fileChooser.heightVersion(valueCallback);
            ((Activity) context).startActivityForResult(i, ShellConfig.REQUEST_SELECT_FILE_HEIGHT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            fileChooser.heightVersion(null);
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void lowFileChooser(String accept, String capture) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        if ("".equals(accept)) {
            i.setType("*/*");
        } else {
            i.setType(accept);
        }
        ((Activity) context).startActivityForResult(Intent.createChooser(i, "选择操作"), ShellConfig.REQUEST_SELECT_FILE_LOW);
    }
}
