package com.example.blinkchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.example.blinkchat.R
import com.example.blinkchat.models.User

import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

const val UID = "uid"
const val NAME = "name"
const val IMAGE = "photo"


class ChatActivity : AppCompatActivity() {

    private val friendId: String by lazy {
        intent.getStringExtra(UID) ?: ""
    }
    private val name: String by lazy {
        intent.getStringExtra(NAME) ?: ""
    }
    private val image: String by lazy {
        intent.getStringExtra(IMAGE) ?: ""
    }
    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid ?: ""
    }
    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        val nameTv: TextView = findViewById(R.id.nameTv)
        val userImgView: ShapeableImageView = findViewById(R.id.userImgView)

        val userReference =
            FirebaseFirestore.getInstance().collection("users").document(mCurrentUid)
        userReference.get()
            .addOnSuccessListener {
                currentUser = it.toObject(User::class.java) ?: User()

            }
            .addOnFailureListener { exception ->
                // Handle the error if data retrieval fails
                currentUser = User() // Initialize with a default value
            }
        nameTv.text = name
        if (image.isNotEmpty()) {
            Picasso.get().load(image).into(userImgView)
        }
    }
}