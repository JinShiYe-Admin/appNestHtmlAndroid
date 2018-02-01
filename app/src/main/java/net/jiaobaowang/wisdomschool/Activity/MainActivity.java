package net.jiaobaowang.wisdomschool.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
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
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.jiaobaowang.wisdomschool.R;
import net.jiaobaowang.wisdomschool.common.JsToJava;
import net.jiaobaowang.wisdomschool.common.ShellConfig;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SELECT_FILE = 1;
    private final static int REQUEST_SELECT_FILE_OTHER = 2;
    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private Context mContext;
    private WebView mWebView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessage;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        mWebView.loadUrl(ShellConfig.MAIN_URL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // WebView中任何跳转都会走这个方法，我们在这里进行判断，如果是我们约定好的连接，就进行自己的操作，否则就放行
                return false; // 拦截了，如果不拦截就是 view.loadUrl(url)
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            // android < 3.0
            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.i(TAG, "openFileChooser:0");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "选择操作"), REQUEST_SELECT_FILE_OTHER);
            }

            // android >= 3.0
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                Log.i(TAG, "openFileChooser:1 acceptType:" + acceptType);
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                if ("".equals(acceptType)) {
                    i.setType("*/*");
                } else {
                    i.setType(acceptType);
                }
                startActivityForResult(Intent.createChooser(i, "选择操作"), REQUEST_SELECT_FILE_OTHER);
            }

            //android >= 4.1
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.i(TAG, "openFileChooser:2 acceptType:" + acceptType + " capture:" + capture);
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                if ("".equals(acceptType)) {
                    i.setType("*/*");
                } else {
                    i.setType(acceptType);
                }
                startActivityForResult(Intent.createChooser(i, "选择操作"), REQUEST_SELECT_FILE_OTHER);
            }

            // android >= 5.0
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.i(TAG, "onShowFileChooser:0");
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent i = fileChooserParams.createIntent();
                try {
                    startActivityForResult(i, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    uploadMessage = null;
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_FILE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (null == uploadMessage)
                        return;
                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    uploadMessage = null;
                }
                break;
            case REQUEST_SELECT_FILE_OTHER:
                if (null == mUploadMessage)
                    return;
                Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
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
