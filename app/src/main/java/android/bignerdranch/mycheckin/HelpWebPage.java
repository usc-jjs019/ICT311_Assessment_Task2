package android.bignerdranch.mycheckin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class HelpWebPage extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static Intent newIntent(Context packageContext, Uri helpWebUri) {
        Intent intent = new Intent(packageContext, HelpWebPage.class);
        intent.setData(helpWebUri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpwebpage);

        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        mWebView.loadUrl("https://www.wikihow.com/Check-In-on-Facebook");
    }
}
