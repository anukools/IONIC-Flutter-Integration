package io.ionic.starter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.plugin.common.MethodChannel;
import com.getcapacitor.PluginHandle;
import io.flutter.embedding.engine.FlutterEngine;
import java.util.Map;
import android.content.Intent;

public class MyFlutterActivity extends FlutterActivity {

    private static final String CHANNEL = "com.example.channel";
    private MethodChannel methodChannel;



     @Override
    public FlutterEngine provideFlutterEngine(@NonNull android.content.Context context) {
        return FlutterEngineCache.getInstance().get("my_engine"); // ✅ use cached engine
    }

    @Override
public void onResume() {
    super.onResume();

    String message = getIntent().getStringExtra("flutter_message");
    if (message != null && methodChannel != null) {
        methodChannel.invokeMethod("getData", message); // ✅ send to Flutter
    }
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Setup method channel
        methodChannel = new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL);
        methodChannel.setMethodCallHandler((call, result) -> {
            if ("getData".equals(call.method)) {
                result.success("Hello from Android/Ionic!");
            } else if ("sendDataBack".equals(call.method)) {
                Map<String, String> args = call.arguments();

                String message = args.get("message");

                // ✅ Send broadcast to MainActivity
                Intent intent = new Intent("com.ionic.FLUTTER_DATA_BACK");
                intent.putExtra("message", message);
                sendBroadcast(intent);

                result.success(null);

                finish();
                Log.d("FLUTTER_MSG", message);
            }
        });
    }
}
