package com.innisoft.notewithalarm.activity

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.innisoft.notewithalarm.R
import com.innisoft.notewithalarm.databinding.ActivityAlarmRunningBinding

class AlarmRunning : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmRunningBinding
    private val notificationPermissionRequestCode = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAlarmRunningBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        checkNotificationPermission()

        binding.note.text = intent.getStringExtra("note")
        val appBar = binding.toolsBar
        appBar.title = intent.getStringExtra("title")
        setSupportActionBar(appBar)
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val title = intent.getStringExtra("title")
        val note = intent.getStringExtra("note")
        binding.note.text = note
        binding.toolsBar.title = title
        val mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
        mediaPlayer.start()

        binding.done.setOnClickListener {
            mediaPlayer.stop()
            finish()
        }

    }


    private fun sendNotification(context: Context) {
        // Define notification channel ID (same as above)
        val channelId = "my_channel_id"

        // Create a notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.home_outline) // Your app icon
            .setContentTitle("Hey Time to Complete your Task")
            .setContentText("Lets Go and Complete your task")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismiss the notification when tapped
            .build()

        // Get the NotificationManager system service
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Send the notification with a unique ID (e.g., 1)
        notificationManager.notify(1, notification)
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, you can send notifications
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            sendNotification(this)
        } else {
            // Request permission
            requestNotificationPermission()
        }
    }

    // Request the notification permission
    private fun requestNotificationPermission() {
        // Request the permission if it's not already granted
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            notificationPermissionRequestCode
        )
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == notificationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                sendNotification(this)
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}