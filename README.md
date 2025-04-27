# Flutter-Ionic Integration POC

This project demonstrates how to integrate a Flutter module within an Ionic application, allowing bidirectional communication between both frameworks.

## Project Structure

```
├── android/                  # Android native code
│   └── app/
│       └── src/
│           └── main/
│               └── java/
│                   └── io/
│                       └── ionic/
│                           └── starter/
│                               ├── MainActivity.java  # Main integration point
├── src/
│   ├── app/
│   │   └── home/
│   │       └── home.page.ts  # Ionic UI for Flutter interaction
│   └── plugin/
│       └── my-custom-bridge.ts  # TypeScript definitions
├── capacitor-bridge/         # Capacitor bridge plugin
│   ├── src/
│   │   └── index.ts         # Plugin interface
│   └── android/
│       └── src/
│           └── main/
│               └── java/
│                   └── io/
│                       └── ionic/
│                           └── starter/
│                               └── CustomBridgePlugInPlugin.java  # Native implementation
└── flutter_module/           # Flutter module (separate project)
```

## Integration Architecture

The integration uses a hybrid approach with the following components:

1. **Native Bridge**: Custom Capacitor plugin (`CustomBridgePlugInPlugin`) handles communication between Ionic and native Android code
2. **Flutter Engine**: Embedded Flutter engine in the Android app
3. **Broadcast Receivers**: Handle communication between Ionic and Flutter
4. **Method Channels**: Enable bidirectional communication between native and Flutter code

## Communication Flow

1. **Ionic → Flutter**:
   - Ionic app triggers Flutter view through native bridge
   - Message is passed via Intent to Flutter activity
   - Flutter receives message through MethodChannel

2. **Flutter → Ionic**:
   - Flutter sends data through MethodChannel
   - Native code broadcasts message via Intent
   - Ionic receives message through BroadcastReceiver

## Setup Instructions

### Prerequisites

- Node.js and npm
- Android Studio
- Flutter SDK
- Ionic CLI
- Capacitor

### Installation Steps

1. **Create Ionic Project**:
   ```bash
   ionic start flutterPOCApp blank --type=angular
   cd flutterPOCApp
   ```

2. **Add Capacitor**:
   ```bash
   npm install @capacitor/core @capacitor/android
   npx cap init
   ```

3. **Create Flutter Module**:
   ```bash
   flutter create --template module flutter_module
   ```

4. **Build Flutter Module**:
   ```bash
   cd flutter_module
   flutter build aar
   ```

5. **Add Flutter Dependencies**:
   Add the following to `android/app/build.gradle`:
   ```gradle
   dependencies {
       implementation project(':flutter')
   }
   ```

6. **Configure MainActivity**:
   - Extend `BridgeActivity`
   - Initialize Flutter engine
   - Set up method channels
   - Register broadcast receivers

## Usage

1. **Launch Flutter from Ionic**:
   ```typescript
   // In Ionic component
   import MyCustomBridge from '../plugin/my-custom-bridge';

   launchFlutter() {
     MyCustomBridge.showFlutterView({ message: 'Hello from Ionic!' });
   }
   ```

2. **Receive Messages in Flutter**:
   ```dart
   // In Flutter
   static const platform = MethodChannel('com.example.channel');
   
   Future<void> _receiveMessage() async {
     try {
       final String result = await platform.invokeMethod('getData');
       print('Received from Ionic: $result');
     } catch (e) {
       print('Error: $e');
     }
   }
   ```

## Key Components

### 1. MainActivity.java
- Manages Flutter engine lifecycle
- Handles communication between Ionic and Flutter
- Registers broadcast receivers for message passing

### 2. MyCustomBridgePlugin
- Custom Capacitor plugin for native communication
- Handles message passing between Ionic and native code

### 3. Flutter Module
- Contains Flutter UI and business logic
- Communicates with native code through method channels

## Troubleshooting

1. **Flutter Engine Not Initializing**:
   - Check Flutter SDK path
   - Verify Flutter module build
   - Ensure proper dependency configuration

2. **Communication Issues**:
   - Verify method channel names match
   - Check broadcast receiver registration
   - Ensure proper message format

3. **Build Errors**:
   - Clean and rebuild project
   - Verify Gradle configuration
   - Check Flutter module integration

## Best Practices

1. **Message Format**:
   - Use consistent message structure
   - Implement error handling
   - Validate data types

2. **Performance**:
   - Initialize Flutter engine once
   - Cache Flutter engine instance
   - Clean up resources properly

3. **Security**:
   - Validate incoming messages
   - Use secure communication channels
   - Implement proper error handling

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License.

## Native Communication with CustomBridgePlugInPlugin

### Sending Messages to Native

```typescript
// In Ionic component
import { CustomBridgePlugInPlugin } from 'capacitor-bridge';

// Send message to native
async function sendToNative() {
  try {
    await CustomBridgePlugInPlugin.sendMessage({
      message: 'Hello from Ionic to Native!'
    });
  } catch (error) {
    console.error('Error sending message:', error);
  }
}
```

### Receiving Messages from Native

```typescript
// In Ionic component
import { CustomBridgePlugInPlugin } from 'capacitor-bridge';

// Listen for native messages
CustomBridgePlugInPlugin.addListener('onNativeMessage', (data: { message: string }) => {
  console.log('Received from Native:', data.message);
  // Handle the message
});
```

### Native Implementation (Android)

```java
// In MainActivity.java
private CustomBridgePlugInPlugin customPluginRef;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Get plugin reference
    PluginHandle pluginHandle = getBridge().getPlugin("CustomBridgePlugInPlugin");
    if (pluginHandle != null && pluginHandle.getInstance() instanceof CustomBridgePlugInPlugin) {
        customPluginRef = (CustomBridgePlugInPlugin) pluginHandle.getInstance();
    }

    // Send message to Ionic
    customPluginRef.sendMessageToJS("Hello from Native!");
}
```

### Message Types

The plugin supports the following message types:
1. **Text Messages**: Simple string communication
2. **JSON Data**: Structured data exchange
3. **Events**: Custom event notifications

### Error Handling

```typescript
// Error handling example
try {
    await CustomBridgePlugInPlugin.sendMessage({
        message: 'Test message'
    });
} catch (error) {
    if (error.code === 'PLUGIN_ERROR') {
        console.error('Plugin error:', error.message);
    } else {
        console.error('Unknown error:', error);
    }
}
``` 