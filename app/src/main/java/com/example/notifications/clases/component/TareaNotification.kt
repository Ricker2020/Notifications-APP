package com.example.notifications.clases.component

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.notifications.MainActivity
import com.example.notifications.clases.adapter.RcVwAdapterTareas
import com.example.notifications.clases.entity.Seccion
import com.example.notifications.clases.entity.Tarea

class TareaNotification : BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val MY_CHANNEL_ID=intent.getStringExtra("channel")!!
        val bundle = intent.extras ?: Bundle()

        val title=bundle.getString("title") ?: "Default Title"
        val resume=bundle.getString("resume") ?: "Default Resume"
        val bigText=bundle.getString("bigText") ?: "Default Big Text"

        createSimpleNotification(context,title,resume,bigText,MY_CHANNEL_ID)
    }

    private fun createSimpleNotification(context: Context, title:String, resume: String, bigText:String, MY_CHANNEL_ID:String) {
        val intent = Intent(context, RcVwAdapterTareas::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle(title)
            .setContentText(resume)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(bigText)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}