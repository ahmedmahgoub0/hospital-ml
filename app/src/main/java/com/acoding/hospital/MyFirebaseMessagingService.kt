package com.acoding.hospital

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var patientId: String? = ""

    private fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel"

        // Intent to open MainActivity with the patientId
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("patientId", patientId) // Pass patient ID as extra
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body

            patientId = remoteMessage.data["patient_id"]

            Log.d(TAG, "Message Notification Title: $title")
            Log.d(TAG, "Message Notification Body: $body")
            Log.d(TAG, "Message Notification patientId: $patientId")

            // Display the notification
            showNotification(title, body)
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Handle custom data payload if needed
        }
    }

    companion object {
        private const val TAG = "FuckZidan"
    }
}
