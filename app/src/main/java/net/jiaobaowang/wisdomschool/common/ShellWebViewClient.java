package net.jiaobaowang.wisdomschool.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ShangLinMo on 2018/2/2.
 */

public class ShellWebViewClient extends WebViewClient {
    private static final String TAG = "ShellWebViewClient";
    private Context context;
    private WebView webView;

    public ShellWebViewClient(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    /**
     * WebView中任何跳转都会走这个方法，我们在这里进行判断，如果是我们约定好的连接，就进行自己的操作，否则就放行
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i(TAG, "shouldOverrideUrlLoading:" + url);
        return false;
    }

    /**
     * 页面加载失败(android < 6.0)
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return;
        Log.i(TAG, "onReceivedError:android < 6.0");
        showError(errorCode, description, failingUrl);
    }

    /**
     * 页面加载失败(android >= 6.0)
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.i(TAG, "onReceivedError:android >= 6.0");
        showError(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
    }

    /**
     * 请求失败
     * @param view
     * @param request
     * @param errorResponse
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        Log.i(TAG, "------onReceivedHttpError------");
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
//        new AlertDialog.Builder(context)
//                .setTitle("提示")
//                .setMessage("页面加载失败\nerrorCode " + errorCode + "\ndescription " + description)
//                .setNegativeButton("刷新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        webView.reload();
//                    }
//                })
//                .setPositiveButton("确定", null)
//                .create().show();
    }
}
