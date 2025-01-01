package com.innisoft.notewithalarm.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.innisoft.notewithalarm.fragment.All
import com.innisoft.notewithalarm.fragment.Home
import com.innisoft.notewithalarm.fragment.Personal
import com.innisoft.notewithalarm.fragment.Work


class MyViewPageAdaptor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment=All()
                fragment
            }

            1 -> {
                Personal()
            }

            2 -> {
                Home()
            }

            else -> Work()

        }
    }
}