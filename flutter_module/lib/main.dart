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
  final TextEditingController _controller = TextEditingController();

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
    final messageToSend = _controller.text.trim();
    if (messageToSend.isNotEmpty) {
      // Send message back to Ionic here (MethodChannel or Navigator.pop)
      print('Sending back: $messageToSend');
     await platform.invokeMethod('sendDataBack', {
      'message': messageToSend
    });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Please enter a message!')),
      );
    }

    // ✅ Close Flutter screen
    SystemNavigator.pop();
    // Navigator.of(context).pop(); // <-- closes FlutterActivity
  }

   @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text("Flutter View", style: TextStyle(color: Colors.white)),
          backgroundColor: Colors.blueAccent,
        ),
        body: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                'Received from Ionic',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              Text(
                receivedMessage,
                style: TextStyle(fontSize: 16, color: Colors.blueGrey),
              ),
              const SizedBox(height: 30),
              TextField(
                controller: _controller,
                decoration: InputDecoration(
                  labelText: 'Enter your reply',
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton.icon(
                onPressed: sendBackAndClose,
                icon: const Icon(Icons.send, color: Colors.white),
                label: const Text('Send Back to Ionic', style: TextStyle(color: Colors.white)),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blueAccent,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  textStyle: const TextStyle(fontSize: 18),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}