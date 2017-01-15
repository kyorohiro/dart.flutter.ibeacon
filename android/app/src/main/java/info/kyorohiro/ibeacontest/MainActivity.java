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
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ok");
                    messageResponse.send(jsonMessage.toString());
                }
            } else {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("r", "ok");
                messageResponse.send(jsonMessage.toString());
            }
        } catch (Exception e) {
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("r", "ng");
            } catch (JSONException ee) {
            }
            messageResponse.send(jsonMessage.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionResponse.containsKey(requestCode)) {
            FlutterView.MessageResponse messageResponse = permissionResponse.remove(requestCode);
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ok");
                    messageResponse.send(jsonMessage.toString());
                } else {
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ng");
                    messageResponse.send(jsonMessage.toString());
                }
            } catch (Exception e) {
                JSONObject jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("r", "ng");
                } catch (JSONException ee) {
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
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ok");
                    messageResponse.send(jsonMessage.toString());
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ng");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.stopLescan", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.stopLescan();
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ok");
                    messageResponse.send(jsonMessage.toString());
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ng");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.getFoundBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    messageResponse.send(beacon.getFoundedBeeaconAsJSONText());
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("r", "ok");
                    messageResponse.send(jsonMessage.toString());
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ng");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                }
            }
        });
        flutterView.addOnMessageListenerAsync("beacon.clearFoundBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.clearFoundedBeeacon();
                    messageResponse.send(beacon.getFoundedBeeaconAsJSONText());
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ng");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                }
            }
        });

        flutterView.addOnMessageListenerAsync("beacon.stopAdvertiseBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, FlutterView.MessageResponse messageResponse) {
                try {
                    beacon.stopAdvertise();
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ok");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "ng");
                    } catch(Exception ee) {
                        messageResponse.send(jsonMessage.toString());
                    }
                }
            }
        });

        flutterView.addOnMessageListenerAsync("beacon.startAdvertiseBeacon", new FlutterView.OnMessageListenerAsync() {
            @Override
            public void onMessage(FlutterView flutterView, String message, final FlutterView.MessageResponse messageResponse) {
                try {
                    final byte[] cont = TinyIBeaconPacket.getUUIDBytesAsIBeacon("AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEEEEEE");

                    beacon.startAdvertise(MainActivity.this, cont, 1,2,-60,new TinyBeacon.AdvertiseCallback(){
                        @Override
                        public void onStartSuccess(TinyBeacon.AdvertiseCallbackParam advertiseCallbackParam) {
                            JSONObject jsonMessage = new JSONObject();
                            try {
                                jsonMessage.put("r", "1ok");
                            } catch(Exception ee) {
                            }
                            messageResponse.send(jsonMessage.toString());
                        }

                        @Override
                        public void onStartFailure(int i) {
                            JSONObject jsonMessage = new JSONObject();
                            try {
                                jsonMessage.put("r", "2ng"+cont.toString()+":"+i);
                            } catch(Exception ee) {
                            }
                            messageResponse.send(jsonMessage.toString());

                        }
                    });
                } catch(Exception e) {
                    JSONObject jsonMessage = new JSONObject();
                    try {
                        jsonMessage.put("r", "3ng"+":"+e.toString());
                    } catch(Exception ee) {
                    }
                    messageResponse.send(jsonMessage.toString());

                }
            }
        });
    }
}
