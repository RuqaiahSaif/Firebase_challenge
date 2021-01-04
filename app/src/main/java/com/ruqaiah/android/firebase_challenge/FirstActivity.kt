package com.ruqaiah.android.firebase_challenge

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.content_main.*

class FirstActivity : AppCompatActivity() {
    private var mAdapter: RecyclerViewAdapter? = null
    private var firestoreListener: ListenerRegistration? = null
    lateinit var firestoreDB: FirebaseFirestore
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(findViewById(R.id.toolbar))
        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()
        firestoreListener = firestoreDB!!.collection("notes")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val notesList = mutableListOf<Note>()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Note::class.java)
                        notesList.add(note)
                    }
                }

                mAdapter =RecyclerViewAdapter(notesList, applicationContext, firestoreDB!!)
                note_recycler_view.adapter = mAdapter
            })


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
val dialog= AddData.newInstance()
            dialog.show(supportFragmentManager,"add data")
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
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

