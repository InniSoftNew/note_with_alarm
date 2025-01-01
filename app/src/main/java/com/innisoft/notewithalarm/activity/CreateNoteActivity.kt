package com.innisoft.notewithalarm.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.innisoft.notewithalarm.R
import com.innisoft.notewithalarm.databinding.ActivityCreateNoteBinding

class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        val appBar = binding.toolsBar
        appBar.title = "Create Note"
        setSupportActionBar(appBar)
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val spinner = binding.spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            R.layout.drop_down_item
        )
        spinner.adapter = adapter



        binding.apply {
            val noteTitle1 = intent.getStringExtra("title")
            val noteBody2 = intent.getStringExtra("note")
            val noteId = intent.getStringExtra("noteId")
            val category = intent.getStringExtra("category")
            val setAlarm = intent.getBooleanExtra("setAlarm", false)
            val alarmTime = intent.getLongExtra("alarmTime", 0)

            noteTitle.setText(noteTitle1)
            noteBody.setText(noteBody2)
            if (category != null) {
                saveNote.text="Update Note"
            }
            saveNote.setOnClickListener {
                if (noteTitle1 != null) {
                    if (noteBody2 != null) {
                        if (noteId != null) {
                            if (category != null) {
                                if (setAlarm != null) {
                                    if (alarmTime != null) {

                                        val category = spinner.selectedItem.toString().lowercase()
                                        val title = noteTitle.text.toString()
                                        val note = noteBody.text.toString()
                                        updateNote(
                                            title,
                                            note,
                                            noteId,
                                            category,
                                            setAlarm,
                                            alarmTime
                                        )
                                    }
                                }
                            }

                        }
                    }
                } else{
                    saveNote.text="Save Note"
                    val category = spinner.selectedItem.toString().lowercase()
                    val title = noteTitle.text.toString()
                    val note = noteBody.text.toString()
                    if (title.isEmpty()) {
                        noteTitle.error = "title is required"
                        noteTitle.requestFocus()
                    } else if (note.isEmpty()) {
                        noteBody.error = "note is required"
                        noteBody.requestFocus()
                    } else {
                        saveData(title, note, category)
                    }
                }

            }
        }
    }

    private fun updateNote(
        noteTitle1: String,
        noteBody2: String,
        noteId: String,
        category: String,
        alarm: Boolean,
        alarmTime: Long
    ) {
        val data = hashMapOf(
            "title" to noteTitle1,
            "note" to noteBody2,
            "category" to category,
            "setAlarm" to alarm,
            "alarmTime" to alarmTime
        )
        db.collection("Notes").document(Firebase.auth.currentUser?.uid!!).collection("myNotes")
            .document(noteId).update(
            data as Map<String, Any>
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "note updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "error" + it.exception, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun saveData(title: String, note: String, category: String) {

        val id = Firebase.auth.currentUser?.uid
        val data = hashMapOf(
            "title" to title,
            "note" to note,
            "category" to category,
            "setAlarm" to false,
            "alarmTime" to System.currentTimeMillis()

        )


        db.collection("Notes").document(id!!).collection("myNotes").document().set(data as Map<String, Any>)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "note added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "error" + task.exception, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}