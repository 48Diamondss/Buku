package com.example.buku.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.buku.Handler.Constant;
import com.example.buku.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // ini class buat web view
        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        // diambil dari Class Constant
        webView.loadUrl(Constant.url_web_view);

        getSupportActionBar().hide();

    }
}