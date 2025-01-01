package com.innisoft.notewithalarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.innisoft.notewithalarm.adaptor.NoteAdaptor
import com.innisoft.notewithalarm.databinding.FragmentPersonalBinding
import com.innisoft.notewithalarm.model.NoteModel


class Personal : Fragment() {
    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val list = ArrayList<NoteModel>()
    private val id = Firebase.auth.currentUser?.uid
    private lateinit var adapter: NoteAdaptor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        adapter = NoteAdaptor(context, list)

        binding.recyclerViewAll.adapter = adapter
        db.collection("Notes").document(id!!).collection("myNotes")
            .whereEqualTo("category", "personal").get().addOnSuccessListener {
                for (i in it) {
                    val title = i.getString("title")
                    val note = i.getString("note")
                    val category = i.getString("category")
                    val setAlarm=i.getBoolean("setAlarm")
                    val alarmTime=i.getLong("alarmTime")
                    val id = i.id
                    val model = NoteModel(title, note, category, id,setAlarm!!,alarmTime!!)
                    list.add(model)

                }

                adapter.notifyDataSetChanged()

                // check list is empty or not
                if (list.isEmpty()) {
                    binding.emptyNote.visibility = View.VISIBLE
                    binding.recyclerViewAll.visibility = View.GONE
                } else {
                    binding.emptyNote.visibility = View.GONE
                    binding.recyclerViewAll.visibility = View.VISIBLE
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "error" + it.message, Toast.LENGTH_SHORT).show()
            }


    }

    override fun onStart() {
        super.onStart()

    }

}