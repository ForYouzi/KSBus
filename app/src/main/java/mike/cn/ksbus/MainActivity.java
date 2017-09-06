package mike.cn.ksbus;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.just.library.AgentWeb;

import mike.cn.ksbus.config.Config;

public class MainActivity extends AppCompatActivity {

    private AgentWeb agentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        agentWeb = AgentWeb.with(this)
                           .setAgentWebParent(layout, new RelativeLayout.LayoutParams(-1, -1))
                           .useDefaultIndicator()
                           .defaultProgressBarColor()
                           .setWebViewClient(mWebViewClient)
                           .addJavascriptInterface("local_obj", new InJavaScriptLocalObj())
                           .createAgentWeb()
                           .go("http://wap.ksbus.com.cn/mapHome");
    }

    //WebViewClient
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            agentWeb.getJsEntraceAccess().callJs(getOutCss(Config.CSS_URL));
        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            agentWeb.getJsEntraceAccess().callJs(getOutCss(Config.CSS_URL));
        }
    };

    final class InJavaScriptLocalObj {
        public void showSource(String html) {
            System.out.println("====>html=" + html);
        }
    }

    public static String getOutCss(String url) {
        String js = ""; js = "javascript:var d=document;" +
                             "var s=d.createElement('link');" +
                             "s.setAttribute('rel', 'stylesheet');" +
                             "s.setAttribute('href', '" + url + "');" +
                             "d.head.appendChild(s);"; return js;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (agentWeb.getWebCreator().get().canGoBack()) {
                agentWeb.getWebCreator().get().goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
};