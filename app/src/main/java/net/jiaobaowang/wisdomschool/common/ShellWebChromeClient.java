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

import net.jiaobaowang.wisdomschool.R;
import net.jiaobaowang.wisdomschool.shell_interface.FileChooser;

import java.util.Arrays;

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

    /**
     * 文件选取(android < 3.0)
     */
    protected void openFileChooser(ValueCallback<Uri> valueCallback) {
        Log.i(TAG, "openFileChooser:android < 3.0");
        fileChooser.lowVersion(valueCallback);
        shellFileChooser("", "");
    }

    /**
     * 文件选取(android >= 3.0)
     */
    protected void openFileChooser(ValueCallback valueCallback, String acceptType) {
        Log.i(TAG, "openFileChooser:android >= 3.0" + "\n"
                + "acceptType:" + acceptType);
        fileChooser.lowVersion(valueCallback);
        shellFileChooser(acceptType, "");
    }

    /**
     * 文件选取(android >= 4.1)
     */
    protected void openFileChooser(ValueCallback<Uri> valueCallback, String accept, String capture) {
        Log.i(TAG, "openFileChooser:android >= 4.1" + "\n"
                + "acceptType:" + accept + "\n"
                + "capture:" + capture);
        fileChooser.lowVersion(valueCallback);
        shellFileChooser(accept, capture);
    }

    /**
     * 文件选取(android >= 5.0)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        Log.i(TAG, "onShowFileChooser:android >= 5.0");
        Log.i(TAG, "FileChooserParams:" + "\n"
                + "getAcceptTypes:" + Arrays.toString(fileChooserParams.getAcceptTypes()) + "\n"
                + "getFilenameHint:" + fileChooserParams.getFilenameHint() + "\n"
                + "getMode:" + fileChooserParams.getMode() + "\n"
                + "getTitle:" + fileChooserParams.getTitle() + "\n"
                + "isCaptureEnabled:" + fileChooserParams.isCaptureEnabled());

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

    /**
     * 文件选取(android < 5.0)最终调用方法
     *
     * @param accept  选取文件类型
     * @param capture 选取文件方法
     */
    private void shellFileChooser(String accept, String capture) {
        Log.i(TAG, "------shellFileChooser------" + "\n"
                + "accept:" + accept + "\n"
                + "capture:" + capture);
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        if (null == accept || "".equals(accept)) {
            i.setType("*/*");
        } else {
            i.setType(accept);
        }
        ((Activity) context).startActivityForResult(Intent.createChooser(i, context.getResources().getString(R.string.selection_operation)), ShellConfig.REQUEST_SELECT_FILE_LOW);
    }

    private void userCameraTakePicture() {
        Log.i(TAG, "用摄像头拍照");
    }

    private void userCameraRecordVideo() {
        Log.i(TAG, "用摄像头录像");
    }
}
