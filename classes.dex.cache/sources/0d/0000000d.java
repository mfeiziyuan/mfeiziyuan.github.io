package com.she.she.she.dlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.List;
import java.util.Random;

/* loaded from: /Users/mac-fl-020/Downloads/yy-main/classes.dex */
public class WebUtils {
    long interval;
    boolean isOpen;
    Random random;
    AutoScrollWebView wv;

    private WebUtils() {
        this.isOpen = false;
        this.interval = 5000L;
    }

    /* loaded from: /Users/mac-fl-020/Downloads/yy-main/classes.dex */
    private static class Holder {
        private static final WebUtils INSTANCE = new WebUtils();

        private Holder() {
        }
    }

    public static WebUtils getInstance() {
        return Holder.INSTANCE;
    }

    public void startWebView(Context context, final List<CodeObject> list, final String str, final Handler handler) {
        if (this.wv == null) {
            this.wv = new AutoScrollWebView(context.getApplicationContext());
        }
        this.wv.setWebViewClient(new WebViewClient() { // from class: com.she.she.she.dlib.WebUtils.1
            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str2, Bitmap bitmap) {
                super.onPageStarted(webView, str2, bitmap);
                WebUtils.this.isOpen = false;
            }
        });
        this.wv.setWebChromeClient(new WebChromeClient() { // from class: com.she.she.she.dlib.WebUtils.2
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(final WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (!WebUtils.this.isOpen && i >= 80) {
                    WebUtils.this.isOpen = true;
                    WebUtils.this.wv.startRandomScroll();
                    final Pair injectJs = WebUtils.this.injectJs(webView, list);
                    if (injectJs == null) {
                        handler.postDelayed(new Runnable() { // from class: com.she.she.she.dlib.WebUtils.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                webView.loadUrl(str);
                            }
                        }, WebUtils.this.randomSleep(3, 5));
                    } else {
                        handler.postDelayed(new Runnable() { // from class: com.she.she.she.dlib.WebUtils.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.e("TAG", "注入url:" + ((String) injectJs.second) + "-----interval: " + injectJs.first);
                                webView.loadUrl((String) injectJs.second);
                            }
                        }, ((Long) injectJs.first).longValue());
                    }
                }
            }
        });
        WebSettings settings = this.wv.getSettings();
        this.wv.clearCache(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(new UserAgentUtils().getWholeUserAgent());
        this.wv.loadUrl(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Pair<Long, String> injectJs(WebView webView, List<CodeObject> list) {
        try {
            if (this.random == null) {
                this.random = new Random();
            }
            for (int i = 0; i < list.size(); i++) {
                CodeObject codeObject = list.get(i);
                if (webView.getUrl().length() < 20 && codeObject.getUrl().length() < 20) {
                    return new Pair<>(Long.valueOf(codeObject.getInterval()), codeObject.getJsStrings().get(this.random.nextInt(list.size())));
                }
                if (webView.getUrl().startsWith(codeObject.getUrl())) {
                    return new Pair<>(Long.valueOf(codeObject.getInterval()), codeObject.getJsStrings().get(this.random.nextInt(list.size())));
                }
            }
            return null;
        } catch (Exception unused) {
            System.out.println("");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int randomSleep(int i, int i2) {
        try {
            return new Random().nextInt((i2 - i) + 1) + i;
        } catch (Exception unused) {
            System.out.println("");
            return 5000;
        }
    }
}