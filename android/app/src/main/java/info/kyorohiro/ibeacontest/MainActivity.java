package info.kyorohiro.ibeacontest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterView;

/**
 * Created by kyorohiro on 2017/01/13.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlutterMain.ensureInitializationComplete(this,null);
        LinearLayout rootLayout = new LinearLayout(this);
        this.setContentView(rootLayout);

        FlutterView flutterView = new FlutterView(this);
        rootLayout.addView(flutterView);

        flutterView.runFromBundle(FlutterMain.findAppBundlePath(getApplicationContext()), null);

        flutterView.addOnMessageListener("callback_sync", new FlutterView.OnMessageListener(){
            @Override
            public String onMessage(FlutterView flutterView, String message) {
                return "hi sync" + message;
            }
        });

        flutterView.addOnMessageListenerAsync("callback_async", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                messageResponse.send("hi async" + message);
            }
        });
    }
}
