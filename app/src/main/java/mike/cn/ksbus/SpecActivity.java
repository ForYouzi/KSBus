package mike.cn.ksbus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import mike.cn.ksbus.config.Config;
import mike.cn.ksbus.network.Spec;
import mike.cn.ksbus.util.HttpUtils;

public class SpecActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_spec);

        textView = (TextView) findViewById(R.id.textView);

        progressDialog = new ProgressDialog(SpecActivity.this);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = START;
                handler.sendMessage(message);

                String content = "";

                switch (Config.SPEC_TYPE) {
                    case Config.MIKE_SPEC:
                        for (int i = 0; i < Config.MIKE_SPEC_DESCS.length; i++) {
                            content += Config.MIKE_SPEC_DESCS[i] + "\n";
                            content += Spec.filter(HttpUtils.executeHttpGet(Config.MIKE_SPEC_URLS[i]));
                            content += "\n";
                        }
                        break;
                    case Config.YOUZI_ON_SPEC:
                        for (int i = 0; i < Config.YOUZI_SPEC_ON_WORK_DESCS.length; i++) {
                            content += Config.YOUZI_SPEC_ON_WORK_DESCS[i] + "\n";
                            content += Spec.filter(HttpUtils.executeHttpGet(Config.YOUZI_SPEC_ON_WORK_URLS[i]));
                            content += "\n";
                        }
                        break;
                    case Config.YOUZI_OFF_SPEC:
                        for (int i = 0; i < Config.YOUZI_SPEC_OFF_WORK_DESCS.length; i++) {
                            content += Config.YOUZI_SPEC_OFF_WORK_DESCS[i] + "\n";
                            content += Spec.filter(HttpUtils.executeHttpGet(Config.YOUZI_SPEC_OFF_WORK_URLS[i]));
                            content += "\n";
                        }
                        break;

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
