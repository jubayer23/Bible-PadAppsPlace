package com.biblepadllc.biblepadappsplace;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends BaseActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initToolbar();

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient() {
            ProgressBar progressBar;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressBar == null) {
                    // in standard case YourActivity.this
                    progressBar = (ProgressBar) findViewById(R.id.loadingProgterm);

                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {

                    progressBar.setVisibility(View.INVISIBLE);


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Allow third party cookies for Android Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(myWebView,true);
        }

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setSupportMultipleWindows(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAppCachePath(appCachePath);

        myWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //for downloading directly through download manager
                Log.d("DEBUG",contentDisposition);
                Log.d("DEBUG",mimetype);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setMimeType(mimetype);
                //attachment;filename="Biblia_3D US.apk";filename*=UTF-8''Biblia_3D%20US.apk
                String filenames[] = contentDisposition.split(";");
                String apk_names[] = filenames[1].split("=");
                String apk_name = apk_names[1].substring(1, apk_names[1].length());
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apk_name);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
            }
        });
        myWebView.loadUrl("http://199.168.139.219:8084/AppStore/home.jsp");
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * This is marshmallow runtime Permissions
         * It will ask user for grand permission in queue order[FIFO]
         * If user gave all permission then check whether user device has google play service or not!
         * NB : before adding runtime request for permission Must add manifest permission for that
         * specific request
         * */
        if (RunnTimePermissions.requestForAllRuntimePermissions(this)) {

        }
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}
