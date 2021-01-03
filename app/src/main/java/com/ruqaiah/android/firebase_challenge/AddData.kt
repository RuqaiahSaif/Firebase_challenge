package com.ruqaiah.android.firebase_challenge

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.system.Os.close
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add_data.*
import java.lang.System.exit
import java.util.*


class AddData : DialogFragment() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var collectionRefrence: CollectionReference = db.collection("notes")
    lateinit var title:EditText
    lateinit var content:EditText
    lateinit var cancel:Button
    lateinit var save: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_data, container, false)
        title = view.findViewById(R.id.note_title) as EditText
         content = view.findViewById(R.id.note_text) as EditText
        save = view.findViewById(R.id.save) as Button
        cancel = view.findViewById(R.id.cancel) as Button
        save.setOnClickListener {
            add()
            dismiss()
            }
        cancel.setOnClickListener {
            dismiss()
        }

        return view

    }

    companion object {

        fun newInstance() = AddData()
    }
    fun add(){
        var note = Note(title.text.toString(), content.text.toString())
        db.collection("notes").add(note).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "added successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "failde to add ${it.exception}", Toast.LENGTH_LONG)
                    .show()
                Log.d("test", it.exception.toString())

            }
        }
    }
}

