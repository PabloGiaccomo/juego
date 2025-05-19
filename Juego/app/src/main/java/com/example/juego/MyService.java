package com.example.juego;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyService extends FirebaseMessagingService {

    private static final String TAG = "MyServiceFCM";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Verificar si el mensaje contiene datos.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // Aquí puedes procesar los datos del mensaje.
        }

        // Verificar si el mensaje contiene una notificación.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // Aquí puedes manejar la notificación (por ejemplo, mostrarla si la app está en primer plano).
            // Si la app está en segundo plano, FCM muestra la notificación automáticamente si la carga útil incluye un objeto "notification".
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // Aquí debes enviar este token a tu servidor de aplicaciones si necesitas enviar mensajes a dispositivos específicos.
    }
}