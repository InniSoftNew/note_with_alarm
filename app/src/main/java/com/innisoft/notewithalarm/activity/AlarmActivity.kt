package com.innisoft.notewithalarm.activity

import android.annotation.SuppressLint

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.innisoft.notewithalarm.databinding.ActivityAlarmBinding
import java.util.Calendar
import java.util.Date

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private final val REQUEST_IGNORE_BATTERY_OPTIMIZATION = 100


    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = binding.toolsBar
        appBar.title = "Set Alarm for : " + intent.getStringExtra("title")
        setSupportActionBar(appBar)
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ///
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()




        binding.setAlarmButton.setOnClickListener {

                val year = binding.datePicker.year
                val month = binding.datePicker.month
                val day = binding.datePicker.dayOfMonth
                val hour = binding.timePicker.hour
                val minute = binding.timePicker.minute

                val calendar = Calendar.getInstance()
// Set the date (you can choose a default date like today) and the selected time (hour, minute)

                calendar.set(Calendar.YEAR, year) // Use current year
                calendar.set(Calendar.MONTH, month) // Use current month
                calendar.set(Calendar.DAY_OF_MONTH, day) // Use current day
                calendar.set(Calendar.HOUR_OF_DAY, hour)  // Set the selected hour
                calendar.set(Calendar.MINUTE, minute)  // Set the selected minute
                calendar.set(Calendar.SECOND, 0)  // Set seconds to 0 (optional)
                calendar.set(Calendar.MILLISECOND, 0)  // Set milliseconds

                val timestamp = calendar.timeInMillis
                val date = Date(timestamp)

                val selectedTime = calendar.timeInMillis



                db.collection("Notes").document(auth.currentUser?.uid.toString()).collection("myNotes")
                    .document(intent.getStringExtra("noteId").toString()).update("setAlarm", true)
                db.collection("Notes").document(auth.currentUser?.uid.toString()).collection("myNotes")
                    .document(intent.getStringExtra("noteId").toString())
                    .update("alarmTime", selectedTime)

                finish()

        }


    // Function to request user to disable battery optimization

    }



}



