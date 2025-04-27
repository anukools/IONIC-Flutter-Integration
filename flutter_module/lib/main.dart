import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('com.example.channel');
  String receivedMessage = 'Waiting...';

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(_handleNativeCall);
  }

  Future<void> _handleNativeCall(MethodCall call) async {
    if (call.method == 'getData') {
      setState(() {
        receivedMessage = call.arguments;
      });
    }
  }

  void sendBackAndClose() async {
    await platform.invokeMethod('sendDataBack', {
      'message': 'Hello from Flutter!'
    });

  // ✅ Close Flutter screen
  SystemNavigator.pop();
  // Navigator.of(context).pop(); // <-- closes FlutterActivity
}

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text("Flutter View")),
        body: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text('Received from Ionic: $receivedMessage'),
                ElevatedButton(
                  onPressed: () {
                    sendBackAndClose();
                   
                  },
                  child: Text('Send Hello - Back to IONIC'),
                )
              ],
            ),
          ),
      ),
    );
  }
}