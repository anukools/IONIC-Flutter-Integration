import { registerPlugin } from '@capacitor/core';

export interface MyCustomBridgePlugin {
  showFlutterView(options: { message: string }): Promise<void>;
  addListener(
    eventName: 'onFlutterMessage',
    listenerFunc: (data: { message: string }) => void
  ): Promise<void>;
}

const MyCustomBridge = registerPlugin<MyCustomBridgePlugin>('MyCustomBridge');

export default MyCustomBridge; 