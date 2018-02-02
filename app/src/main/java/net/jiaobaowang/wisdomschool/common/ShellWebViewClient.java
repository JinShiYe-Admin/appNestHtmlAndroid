package net.jiaobaowang.wisdomschool.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ShangLinMo on 2018/2/2.
 */

public class ShellWebViewClient extends WebViewClient {
    private static final String TAG = "ShellWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // WebView中任何跳转都会走这个方法，我们在这里进行判断，如果是我们约定好的连接，就进行自己的操作，否则就放行
        return false; // 拦截了，如果不拦截就是 view.loadUrl(url)
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return;
        Log.i(TAG, "onReceivedError:android < 6.0");
        showError(errorCode, description, failingUrl);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.i(TAG, "onReceivedError:android >= 6.0");
        showError(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
    }

    /**
     * 页面加载失败的处理
     *
     * @param errorCode   失败代码
     * @param description 失败描述
     * @param failingUrl  失败Url
     */
    private void showError(int errorCode, String description, String failingUrl) {
        Log.i(TAG, "------showError------" + "\n"
                + "errorCode:" + errorCode + "\n"
                + "description:" + description + "\n"
                + "failingUrl:" + failingUrl);
    }
}
