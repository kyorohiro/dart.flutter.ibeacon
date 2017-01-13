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
    }
}
