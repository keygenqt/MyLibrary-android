package com.keygenqt.mylibrary.base

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keygenqt.mylibrary.R

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class BaseFirebaseMessaging : FirebaseMessagingService() {

    companion object {
        fun getToken(delegate: (String) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(
                        "BaseFirebaseMessaging",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                } else {
                    delegate.invoke(task.result.toString())
                }
            })
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.channelId ?: "channelId", it.title ?: "", it.body ?: "", remoteMessage.data["uri"])
        }
    }

    private fun sendNotification(channelId: String, title: String, messageBody: String, uri: String?) {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_local_library)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph_app)
                .setDestination(R.id.FragmentSplash)
                .setArguments(Bundle().apply {
                    uri?.let {
                        putString("uri", uri)
                    }
                })
                .createPendingIntent())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}