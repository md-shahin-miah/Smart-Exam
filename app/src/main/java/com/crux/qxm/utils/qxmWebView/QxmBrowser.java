package com.crux.qxm.utils.qxmWebView;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class QxmBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
