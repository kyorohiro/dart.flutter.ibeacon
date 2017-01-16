package info.kyorohiro.ibeacontest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import info.kyorohiro.tinybeacon.TinyAdPacket;
import info.kyorohiro.tinybeacon.TinyBeacon;
import info.kyorohiro.tinybeacon.TinyIBeaconPacket;
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
    TinyBeacon  beacon = new TinyBeacon();
    void requestLocationPermission(final FlutterView.MessageResponse messageResponse) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionResponse.put(requestIdBase++, messageResponse);
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestIdBase - 1);
                } else {
                    messageResponse.send("{\"r\":\"ok\"}");
                }
            } else {
                messageResponse.send("{\"r\":\"ok\"}");
            }
        } catch (Exception e) {
            messageResponse.send("{\"r\":\"ng\"}");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionResponse.containsKey(requestCode)) {
            FlutterView.MessageResponse messageResponse = permissionResponse.remove(requestCode);
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    messageResponse.send("{\"r\":\"ok\"}");
                } else {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            } catch (Exception e) {
                messageResponse.send("{\"r\":\"ng\"}");
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

        flutterView.addOnMessageListenerAsync("beacon.requestPermission", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                requestLocationPermission(messageResponse);
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.startLescan", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.startLescan(MainActivity.this);
                    messageResponse.send("{\"r\":\"ok\"}");
                } catch(Exception e) {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.stopLescan", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.stopLescan();
                    messageResponse.send("{\"r\":\"ok\"}");
                } catch(Exception e) {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.getFoundBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    messageResponse.send(beacon.getFoundedIBeaconAsJSONText());
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.clearFoundBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.clearFoundedIBeacon();
                    messageResponse.send("{\"r\":\"ok\"}");
                } catch(Exception e) {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });

        flutterView.addOnMessageListenerAsync("beacon.stopAdvertiseBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.stopAdvertiseIBeacon();
                    messageResponse.send("{\"r\":\"ok\"}");
                } catch(Exception e) {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });

        flutterView.addOnMessageListenerAsync("beacon.startAdvertiseBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, final FlutterView.MessageResponse messageResponse) {
                try {
                    final byte[] cont = TinyIBeaconPacket.createUUIDBytes("AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEEEEEE");

                    beacon.startAdvertiseIBeacon(MainActivity.this, cont, 1,2,-60,new TinyBeacon.AdvertiseCallback(){
                        @Override
                        public void onStartSuccess(TinyBeacon.AdvertiseCallbackParam advertiseCallbackParam) {
                            messageResponse.send("{\"r\":\"ok\"}");
                        }

                        @Override
                        public void onStartFailure(int i) {
                            messageResponse.send("{\"r\":\"ng\"}");
                        }
                    });
                } catch(Exception e) {
                    messageResponse.send("{\"r\":\"ng\"}");
                }
            }
        });
    }
}
