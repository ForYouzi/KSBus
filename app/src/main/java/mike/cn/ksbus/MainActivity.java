package mike.cn.ksbus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
                           .createAgentWeb()
                           .go("http://wap.ksbus.com.cn/mapHome");
    }

    //WebViewClient
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            agentWeb.getJsEntraceAccess().callJs(loadCss(Config.CSS_URL));
        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            agentWeb.getJsEntraceAccess().callJs(loadCss(Config.CSS_URL));
        }
    };

    public static String loadCss(String url) {
        return "javascript:var d=document;" +
               "var s=d.createElement('link');" +
               "s.setAttribute('rel', 'stylesheet');" +
               "s.setAttribute('href', '" + url + "');" +
               "d.head.appendChild(s);";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充menu的main.xml文件; 给action bar添加条目
        menu.add(0, Config.MIKE_SPEC, 0, "Mike 回家");
        menu.add(0, Config.YOUZI_ON_SPEC, 0, "Youzi 上班");
        menu.add(0, Config.YOUZI_OFF_SPEC, 0, "Youzi 下班");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), SpecActivity.class);
        Log.i(this.getClass().getCanonicalName(), String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 0:
                Config.SPEC_TYPE = Config.MIKE_SPEC;
                startActivity(intent);
                break;
            case 1:
                Config.SPEC_TYPE = Config.YOUZI_ON_SPEC;
                startActivity(intent);
                break;
            case 2:
                Config.SPEC_TYPE = Config.YOUZI_OFF_SPEC;
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}