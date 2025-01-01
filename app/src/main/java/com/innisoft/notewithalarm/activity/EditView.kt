package com.innisoft.notewithalarm.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.collection.LLRBNode.Color
import com.innisoft.notewithalarm.databinding.ActivityEditViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.format

class EditView : AppCompatActivity() {
    private lateinit var binding: ActivityEditViewBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.note.text = intent.getStringExtra("note")
        val appBar = binding.toolsBar
        appBar.title = intent.getStringExtra("title")
        setSupportActionBar(appBar)
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val alarmTime = intent.getLongExtra("alarmTime", 0)

        val formattedDateTimeCustom = alarmTime.toDateString("dd/MM/yyyy hh:mm a", Locale.US)

        val isSet = intent.getBooleanExtra("setAlarm", false)
        if (isSet) {
            binding.alarmIsSet.text = "Alarm Set  : $formattedDateTimeCustom"
        } else {
            binding.alarmIsSet.text = "Alarm Not Set"
        }
        if (alarmTime<System.currentTimeMillis()){
            binding.alarmIsSet.text = "Task Completed at : $formattedDateTimeCustom"

        }

        binding.note.setOnLongClickListener {

            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("note", binding.note.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "note copied", Toast.LENGTH_SHORT).show()

            true
        }
    }

    fun Long.toDateString(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): String {
        val date = Date(this)
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }

}