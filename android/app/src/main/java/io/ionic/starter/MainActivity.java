package io.ionic.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.getcapacitor.BridgeActivity;
import com.ionic.samplebrige.CustomBridgePlugInPlugin;

import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;
import com.getcapacitor.PluginHandle;



public class MainActivity extends BridgeActivity {
    public static String messageFromIonic = "Default";

    private CustomBridgePlugInPlugin customPluginRef;

    private BroadcastReceiver flutterLaunchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.ionic.START_FLUTTER".equals(intent.getAction())) {
                String message = intent.getStringExtra("message");
                Log.d("FlutterBridge", "Received: " + message);
                launchFlutter(message);
            }
        }
    };

    private final BroadcastReceiver flutterResultReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        Log.d("FlutterBridge", "Received data from Flutter: " + message);

        if (customPluginRef != null) {
            customPluginRef.sendMessageToJS(message);
        } else {
            Log.e("PLUGIN", "customPluginRef is null");
        }
    }
};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Register receiver and plugins
        registerReceiver(flutterLaunchReceiver, new IntentFilter("com.ionic.START_FLUTTER"), Context.RECEIVER_EXPORTED);
        registerReceiver(flutterResultReceiver, new IntentFilter("com.ionic.FLUTTER_DATA_BACK"), Context.RECEIVER_EXPORTED);

        registerPlugin(CustomBridgePlugInPlugin.class);

      

        // Setup Flutter engine
        Log.d("MainActivity", "Setting up Flutter engine...");
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache.getInstance().put("my_engine", flutterEngine);
        Log.d("MainActivity", "Flutter engine cached.");

        // IONIC BRIDGE

        PluginHandle pluginHandle = getBridge().getPlugin("MyCustomBridge");

        if (pluginHandle != null && pluginHandle.getInstance() instanceof CustomBridgePlugInPlugin) {
             customPluginRef = (CustomBridgePlugInPlugin) pluginHandle.getInstance();
            Log.e("PLUGIN", "CustomBridgePlugInPlugin  found");
        } else {
            Log.e("PLUGIN", "CustomBridgePlugInPlugin not found or invalid type");
        }

       
    }

    public void launchFlutter(String message) {
        Log.d("MainActivity", "Launching Flutter with message: " + message);
        

       Intent intent = new Intent(this, MyFlutterActivity.class);
    intent.putExtra("flutter_message", message); // ✅ pass it cleanly
    startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(flutterLaunchReceiver);
        unregisterReceiver(flutterResultReceiver);
    }
}









