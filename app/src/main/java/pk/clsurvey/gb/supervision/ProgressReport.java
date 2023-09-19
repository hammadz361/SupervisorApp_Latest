package pk.clsurvey.gb.supervision;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ProgressReport extends AppCompatActivity {
    private WebView mywebView;
    @SuppressLint({"SuspiciousIndentation", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report);
        Bundle b = getIntent().getExtras();
//        Toast.makeText(ProgressReport.this, b.getString("code"), Toast.LENGTH_LONG).show();
        mywebView=(WebView) findViewById(R.id.webview);
        mywebView.getSettings().setLoadWithOverviewMode(true);
        mywebView.getSettings().setUseWideViewPort(true);
        mywebView.setWebViewClient(new WebViewClient());

        if(b.getString("type").equals("s"))
            mywebView.loadUrl("http://sindh.clsurvey.pk:8088/cluster_progress_report.aspx?type="+b.getString("type")+"&code="+Integer.parseInt(b.getString("code")));
        if(b.getString("type").equals("e"))
            mywebView.loadUrl("http://sindh.clsurvey.pk:8088/cluster_progress_report.aspx?type="+b.getString("type")+"&code="+b.getString("code"));

        WebSettings webSettings=mywebView.getSettings();
//        35322001
        webSettings.setJavaScriptEnabled(true);
    }
    public class mywebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public void onBackPressed(){
        if(mywebView.canGoBack()) {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}