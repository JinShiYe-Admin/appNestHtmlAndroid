package net.jiaobaowang.wisdomschool.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import net.jiaobaowang.wisdomschool.R;
import net.jiaobaowang.wisdomschool.common.JsToJava;
import net.jiaobaowang.wisdomschool.common.ShellConfig;
import net.jiaobaowang.wisdomschool.common.ShellWebChromeClient;
import net.jiaobaowang.wisdomschool.common.ShellWebViewClient;
import net.jiaobaowang.wisdomschool.shell_interface.FileChooser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private Context mContext;
    private WebView mWebView;
    private ValueCallback<Uri> lowValueCallback;
    private ValueCallback<Uri[]> heightValueCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "------onCreate------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mWebView = findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用JavaScript
        mWebView.addJavascriptInterface(new JsToJava(mContext), "native");
        //允许弹出框
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebViewClient(new ShellWebViewClient());
        mWebView.setWebChromeClient(new ShellWebChromeClient(mContext, new FileChooser() {
            @Override
            public void lowVersion(ValueCallback<Uri> valueCallback) {
                lowValueCallback = valueCallback;
            }

            @Override
            public void heightVersion(ValueCallback<Uri[]> valueCallback) {
                heightValueCallback = valueCallback;
            }
        }));
        mWebView.loadUrl(ShellConfig.MAIN_URL);
//        File file = this.getApplicationContext().getCacheDir().getAbsoluteFile();
//        Log.e(TAG, "缓存文件：" + file.getAbsolutePath());
//        if (file.exists()) {
//            Log.e(TAG, "有需要清除的文件");
//            if (file.isFile()) {
//                Log.e(TAG, "isFile:" + file.getName());
//            } else if (file.isDirectory()) {
//                Log.e(TAG, "isDirectory:" + file.getName());
//                File files[] = file.listFiles();
//                Log.e(TAG, "files:" + files.length);
//            }
//        } else {
//            Log.e(TAG, "没有需要清除的文件");
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ShellConfig.REQUEST_SELECT_FILE_LOW:
                if (null != lowValueCallback) {
                    Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null : data.getData();
                    lowValueCallback.onReceiveValue(result);
                    lowValueCallback = null;
                }
                break;
            case ShellConfig.REQUEST_SELECT_FILE_HEIGHT:
                if (null != heightValueCallback && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    heightValueCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    heightValueCallback = null;
                }
                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    System.exit(0);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
