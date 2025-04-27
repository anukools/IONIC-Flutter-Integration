import { Component, OnInit } from '@angular/core';
import { IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonCard, IonCardContent, IonCardHeader  } from '@ionic/angular/standalone';

// import MyCustomBridge from '../plugin/my-custom-bridge';
import  MyCustomBridge   from 'capacitor-bridge'; // ✅ Import your bridge

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonCard, IonCardContent, IonCardHeader],
})
export class HomePage implements OnInit {
  constructor() {}

  flutterMessage: string = 'No message yet';

  ngOnInit() {
    // Listen for messages from Flutter
    MyCustomBridge.addListener('onFlutterMessage', (data: { message: string }) => {
      console.log('📩 Received from Flutter:', data.message);
      // You can also update UI or show a toast here
      this.flutterMessage = data.message;
    });
  }

  launchFlutter() {
    console.log('launchFlutter:');
    MyCustomBridge.showFlutterView({ message: 'Hi from Ionic!' });
  }
}
