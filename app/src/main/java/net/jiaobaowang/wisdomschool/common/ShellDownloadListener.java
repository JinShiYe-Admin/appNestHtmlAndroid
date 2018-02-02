package net.jiaobaowang.wisdomschool.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.DownloadListener;

import net.jiaobaowang.wisdomschool.R;

/**
 * 下载监听
 * Created by ShangLinMo on 2018/2/2.
 */

public class ShellDownloadListener implements DownloadListener {
    private Context context;

    public ShellDownloadListener(Context context) {
        this.context = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
        Log.i("ShellDownloadListener", "------onDownloadStart------" + "\n"
                + "url:" + url + "\n"
                + "userAgent:" + userAgent + "\n"
                + "contentDisposition:" + contentDisposition + "\n"
                + "mimeType:" + mimeType + "\n"
                + "contentLength:" + contentLength);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.selection_operation)));
    }
}
