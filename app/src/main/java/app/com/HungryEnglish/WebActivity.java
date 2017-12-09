package app.com.HungryEnglish;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityWebBinding;

import static app.com.HungryEnglish.Util.RestConstant.HUNGRY_ENGLISH_PRIVACY;

public class WebActivity extends AppCompatActivity {
    public static String URL = "url";
    private ActivityWebBinding binding;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(URL, HUNGRY_ENGLISH_PRIVACY);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null) {
            String url = getIntent().getStringExtra(URL);
            binding.webView.loadUrl(HUNGRY_ENGLISH_PRIVACY);
        }
        binding.webView.loadUrl(HUNGRY_ENGLISH_PRIVACY);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
