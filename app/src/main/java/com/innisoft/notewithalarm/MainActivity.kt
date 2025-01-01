package com.innisoft.notewithalarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.color.DynamicColors
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.innisoft.notewithalarm.activity.CreateNoteActivity
import com.innisoft.notewithalarm.adaptor.MyViewPageAdaptor
import com.innisoft.notewithalarm.alarm.AlarmReceiver
import com.innisoft.notewithalarm.databinding.ActivityMainBinding
import com.innisoft.notewithalarm.model.ModelForAlarm







class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptor: MyViewPageAdaptor
    private lateinit var db: FirebaseFirestore
    private lateinit var viewPager2: ViewPager2
    private lateinit var auth: FirebaseAuth
    private var alarmTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = binding.toolbar
        setSupportActionBar(appBar)
        DynamicColors.applyToActivityIfAvailable(this)
        appBar?.setNavigationOnClickListener {
            onBackPressed()
        }
        auth = Firebase.auth
        viewPager2 = binding.viewPager
        adaptor = MyViewPageAdaptor(supportFragmentManager, lifecycle)
        viewPager2.adapter = adaptor
        db = Firebase.firestore
        //tab view end
        tabView()
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, CreateNoteActivity::class.java))
        }
        startAlarm()

    }

    private fun tabView() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab?.position!!
                when (tab.position) {
                    0 -> {
                        tab.text = "All note"
                    }

                    1 -> {
                        tab.text = "Personal"
                    }

                    2 -> {
                        tab.text = "Home"
                    }

                    3 -> {
                        tab.text = "Work"
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            tab.text = ""
                        }

                        1 -> {
                            tab.text = ""
                        }

                        2 -> {
                            tab.text = ""
                        }

                        3 -> {
                            tab.text = ""
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                if (position != 0) {
                    //  binding.topLay.visibility = View.GONE
                } else {
                    //  binding.topLay.visibility = View.VISIBLE
                }
            }

        })
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem == 0) {
            super.onBackPressed()
        } else {
            if (viewPager2.currentItem > 2) {
                viewPager2.setCurrentItem(0, true)
            } else {
                viewPager2.setCurrentItem(viewPager2.currentItem - 1, true)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tools_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                Toast.makeText(applicationContext, "settings", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }

    private fun startAlarm() {
        db.collection("Notes").document(auth.currentUser?.uid.toString()).collection("myNotes")
            .get().addOnSuccessListener {
                for (i in it) {
                    val title = i.getString("title")
                    val note = i.getString("note")
                    val category = i.getString("category")
                    val setAlarm = i.getBoolean("setAlarm")
                    val id = i.id
                    alarmTime = i.getLong("alarmTime")!!
                    if (setAlarm == true) {
                        val data = ModelForAlarm(title, note, category, id, setAlarm, alarmTime!!)
                        if (alarmTime!! > System.currentTimeMillis()) {
                            scheduleAlarmClock(this, data.alarmTime, 1)
                        }

                    }


                }
            }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarmClock(context: Context, triggerTimeMillis: Long, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )

    }

}