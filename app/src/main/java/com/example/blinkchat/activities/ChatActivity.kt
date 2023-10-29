package com.example.blinkchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.blinkchat.R
import com.example.blinkchat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

const val UID = "uid"
const val NAME = "name"
const val IMAGE = "photo"


class ChatActivity : AppCompatActivity() {

    private val friendId: String by lazy {
        intent.getStringExtra(UID)!!
    }
    private val name: String by lazy {
        intent.getStringExtra(NAME)!!
    }
    private val image: String by lazy {
        intent.getStringExtra(IMAGE)!!
    }
    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }
    private val db:FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_chat)

    }
}