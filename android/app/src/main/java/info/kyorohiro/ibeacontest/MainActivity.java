package info.kyorohiro.ibeacontest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kyorohiro on 2017/01/13.
 */

public class MainActivity extends Activity {
    int requestIdBase = 0;
    Map<Integer, FlutterView.MessageResponse> permissionResponse = new HashMap();

    void requestLocationPermission(final FlutterView.MessageResponse messageResponse) {
//        messageResponse.send("hi async" );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // need permission
               permissionResponse.put(requestIdBase++,messageResponse);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestIdBase-1);
            } else {
                JSONObject jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("r", "ok");
                } catch (JSONException e) {
                }
                messageResponse.send(jsonMessage.toString());
            }
        } else {
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("r", "ok");
            } catch (JSONException e) {
            }
            messageResponse.send(jsonMessage.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissionResponse.containsKey(requestCode)){
            FlutterView.MessageResponse messageResponse = permissionResponse.remove(requestCode);
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                JSONObject jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("r", "ok");
                } catch(JSONException e){
                }
                messageResponse.send(jsonMessage.toString());
            } else {
                JSONObject jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("r", "ok");
                } catch(JSONException e){
                }
                messageResponse.send(jsonMessage.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlutterMain.ensureInitializationComplete(this,null);
        LinearLayout rootLayout = new LinearLayout(this);
        this.setContentView(rootLayout);

        FlutterView flutterView = new FlutterView(this);
        rootLayout.addView(flutterView);

        flutterView.runFromBundle(FlutterMain.findAppBundlePath(getApplicationContext()), null);

        flutterView.addOnMessageListenerAsync("requestPermission", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                requestLocationPermission(messageResponse);
            }
        });

    }
}
