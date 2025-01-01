package com.innisoft.notewithalarm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.innisoft.notewithalarm.activity.AlarmRunning

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i=Intent(context, AlarmRunning::class.java)

        i.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(i)
    }

}