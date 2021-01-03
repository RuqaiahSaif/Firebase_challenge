package com.ruqaiah.android.firebase_challenge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerViewAdapter(private val notesList: MutableList<Note>,
                          private val context: Context,
                          private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.note_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val note = notesList[position]

        holder!!.title.text = note.title
        holder.content.text = note.content
    }

    override fun getItemCount(): Int {
        return notesList.size
    }




    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var content: TextView


        init {
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)


        }
    }
}