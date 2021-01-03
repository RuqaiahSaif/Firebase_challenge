package com.ruqaiah.android.firebase_challenge

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_main.*

class FirstActivity : AppCompatActivity() {
    private var mAdapter: RecyclerViewAdapter? = null

    lateinit var firestoreDB: FirebaseFirestore
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(findViewById(R.id.toolbar))
        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
val dialog= AddData.newInstance()
            dialog.show(supportFragmentManager,"add data")
        }
    }
    private fun loadNotesList() {
        firestoreDB!!.collection("notes")
            .get()
            .addOnSuccessListener{ task ->

                    val notesList = mutableListOf<Note>()

                    for (doc in task) {
                        val note = doc.toObject<Note>(Note::class.java)

                        notesList.add(note)
                    }

                    mAdapter = RecyclerViewAdapter(notesList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    note_recycler_view.layoutManager = mLayoutManager
                    note_recycler_view.adapter = mAdapter
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
            }
    }

