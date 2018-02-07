package mike.cn.ksbus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import mike.cn.ksbus.config.Config;
import mike.cn.ksbus.network.MikeSpec;
import mike.cn.ksbus.util.HttpUtil;

public class MikeSpecActivity extends AppCompatActivity {

    private TextView textView;

    private static final int START = 1;
    private static final int END   = 2;

    ProgressDialog progressDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case START:
                    progressDialog.show();
                    break;
                case END:
                    progressDialog.dismiss();
                    textView.setText((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mike_spec);

        textView = (TextView) findViewById(R.id.textView);

        progressDialog = new ProgressDialog(MikeSpecActivity.this);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = START;
                handler.sendMessage(message);

                String content = "";

                for (int i = 0; i < Config.MIKE_SPEC_DESCS.length; i++) {
                    content += Config.MIKE_SPEC_DESCS[i] + "\n";
                    content += MikeSpec.filter(HttpUtil.executeHttpGet(Config.MIKE_SPEC_URLS[i]));
                }
                message = new Message();
                message.what = END;
                message.obj = content;
                handler.sendMessage(message);
            }
        });

        thread.start();
    }
}
