package com.innisoft.notewithalarm.adaptor

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.innisoft.notewithalarm.R
import com.innisoft.notewithalarm.activity.AlarmActivity
import com.innisoft.notewithalarm.activity.CreateNoteActivity
import com.innisoft.notewithalarm.activity.EditView
import com.innisoft.notewithalarm.adaptor.NoteAdaptor.NoteViewHolder
import com.innisoft.notewithalarm.model.NoteModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdaptor(var context: Context?, private var list: ArrayList<NoteModel>?) :
    RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.show_note_layout, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        //Note View Set start
        holder.noteTitle.text = list!![position].title
        holder.noteBody.text = list!![position].note
        holder.toolsBar.inflateMenu(R.menu.edit_menu)
        val isAlarm:Boolean=list!![position].setAlarm

        if (isAlarm){
            holder.alarmIcon.drawable.current.setTint(context!!.resources.getColor(android.R.color.holo_blue_dark))
            holder.toolsBar.title="Set"
            holder.toolsBar.setTitleTextColor(context!!.resources.getColor(android.R.color.holo_blue_dark))
        }else{
            holder.alarmIcon.drawable.current.setTint(context!!.resources.getColor(android.R.color.holo_red_dark))
            holder.toolsBar.title="Not Set"
            holder.toolsBar.setTitleTextColor(context!!.resources.getColor(android.R.color.holo_red_dark))
        }
        holder.alarmIcon.setOnClickListener{
            val intent = Intent(context, AlarmActivity::class.java)
            intent.putExtra("title", list!![position].title)
            intent.putExtra("note", list!![position].note)
            intent.putExtra("noteId", list!![position].id)
            intent.putExtra("category", list!![position].category)
            context?.startActivity(intent)
        }
        //Note View Set end
        //


        //Note menu Edit,Delete,Set Alarm start
        holder.toolsBar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.edit -> {
                    openDialogEdit(position)
                    true
                }

                R.id.delete -> {
                    openDialog(position)
                    true
                }

                else -> {
                    true
                }
            }
        }
        //Note menu Edit,Delete,Set Alarm end
        //
        //open note start
        holder.noteLayout.setOnClickListener {
            openNote(position)
        }
        holder.noteTitle.setOnClickListener {
            openNote(position)
        }
        holder.noteBody.setOnClickListener {
            openNote(position)
        }
        //open note end

        if (list!![position].setAlarm) {
            val timestamp: Long = list!![position].alarmTime!!
            val formatedTime = timestamp.toDateString("dd/MM/yyyy hh:mm a", Locale.US)
            if (timestamp < System.currentTimeMillis()) {
//                holder.toolsBar.title = "Task Completed"
//                holder.toolsBar.setTitleTextColor(Color.BLUE)
//                holder.toolsBar.titleMarginStart = 0
//                holder.toolsBar.textAlignment = View.TEXT_ALIGNMENT_CENTER
//

            } else {
//                holder.toolsBar.title = "Alarm Start: \n" + formatedTime
//                holder.toolsBar.setTitleTextColor(Color.BLUE)
//                holder.toolsBar.titleMarginStart = 0
//                holder.toolsBar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

        } else {
            holder.toolsBar.titleMarginStart = 30
        }
    }

    private fun openDialog(position: Int) {
        AlertDialog.Builder(context!!).setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note? \n " + list!![position].title)
            .setPositiveButton("Yes") { dialog, _ ->
                removeNote(position)
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount(): Int {

        return list!!.size
    }

    //My View Holder
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: MaterialTextView = itemView.findViewById(R.id.title)
        var noteBody: MaterialTextView = itemView.findViewById(R.id.body)
        var noteLayout: View = itemView.findViewById(R.id.note_layout)
        var toolsBar: androidx.appcompat.widget.Toolbar = itemView.findViewById(R.id.toolsBar2)
        var alarmIcon: com.google.android.material.imageview.ShapeableImageView =itemView.findViewById(R.id.alarmIcon)


    }//My View Holder end

    private fun removeNote(position: Int) {
        val database = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?.uid
        val id = list!![position].id
        database.collection("Notes").document(userId.toString()).collection("myNotes")
            .document(id!!).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "note deleted", Toast.LENGTH_SHORT).show()
                    list!!.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Toast.makeText(context, "error" + it.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openNote(position: Int) {
        val intent = Intent(context, EditView::class.java)
        intent.putExtra("title", list!![position].title)
        intent.putExtra("note", list!![position].note)
        intent.putExtra("noteId", list!![position].id)
        intent.putExtra("category", list!![position].category)
        intent.putExtra("setAlarm", list!![position].setAlarm)
        intent.putExtra("alarmTime", list!![position].alarmTime)

        context?.startActivity(intent)
    }

    fun Long.toDateString(
        pattern: String = "yyyy-MM-dd HH:mm:ss",
        locale: Locale = Locale.getDefault()
    ): String {
        val date = Date(this)
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }

    private fun openDialogEdit(position: Int) {
        val intent = Intent(context, CreateNoteActivity::class.java)
        intent.putExtra("title", list!![position].title)
        intent.putExtra("note", list!![position].note)
        intent.putExtra("noteId", list!![position].id)
        intent.putExtra("category", list!![position].category)
        intent.putExtra("setAlarm", list!![position].setAlarm)
        intent.putExtra("alarmTime", list!![position].alarmTime)

        context?.startActivity(intent)
    }
    private fun startFadeInAnimation(textView: TextView) {
        val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
        fadeIn.duration = 1000 // Duration in milliseconds
        // Optional: Add an interpolator
        fadeIn.start()
    }
}
