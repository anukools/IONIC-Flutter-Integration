import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StatusBar, Style } from '@capacitor/status-bar';
import { CommonModule } from '@angular/common'; // 🛠️ Add this import!
import {
  IonHeader, 
  IonToolbar, 
  IonTitle, 
  IonContent, 
  IonGrid, 
  IonRow, 
  IonCol, 
  IonCard, 
  IonCardHeader, 
  IonCardTitle, 
  IonCardContent, 
  IonItem, 
  IonLabel, 
  IonInput, 
  IonButton, 
  IonText 
  } from '@ionic/angular/standalone';

// import MyCustomBridge from '../plugin/my-custom-bridge';
import  MyCustomBridge   from 'capacitor-bridge'; // ✅ Import your bridge

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [
    CommonModule,
    FormsModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonGrid,
    IonRow,
    IonCol,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonItem,
    IonLabel,
    IonInput,
    IonButton,
    IonText
  ],
})
export class HomePage implements OnInit {
  constructor() {
    this.setupStatusBar();
  }

  async setupStatusBar() {
    try {
      await StatusBar.setOverlaysWebView({ overlay: false }); // ✅ Very important
      await StatusBar.setBackgroundColor({ color: '#66000000' }); // optional: set toolbar blue background
      // await StatusBar.setStyle({ style: Style.Light }); // optional: status bar text color light
    } catch (error) {
      console.error('StatusBar setup error:', error);
    }
  }
  messageToSend: string = 'Hi from Ionic!';
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
    try {
      MyCustomBridge.showFlutterView({ message: this.messageToSend });
   } catch (error) {
     console.error('Error sending message to Flutter:', error);
   }
  }
}
