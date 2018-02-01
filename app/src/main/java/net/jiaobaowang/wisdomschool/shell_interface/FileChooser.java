package net.jiaobaowang.wisdomschool.shell_interface;

import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * Created by ShangLinMo on 2018/2/1.
 */

public interface FileChooser {
    void lowVersion(ValueCallback<Uri> valueCallback);
    void heightVersion(ValueCallback<Uri[]> valueCallback);
}
