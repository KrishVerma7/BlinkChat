package com.example.blinkchat.activities

import PeopleFragment
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.example.blinkchat.R
import com.example.blinkchat.models.Message
import com.example.blinkchat.models.User

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.play.integrity.internal.e
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
        val sendBtn: ImageView = findViewById(R.id.sendBtn)
        val msgEt: EditText = findViewById(R.id.msgEt)

        val userReference =
            FirebaseFirestore.getInstance().collection("users").document(mCurrentUid)
        userReference.get()
            .addOnSuccessListener {
                currentUser = it.toObject(User::class.java)!!
                Log.d("ChatActivity", "User data retrieved: $currentUser")
                displayUserData(name, nameTv, userImgView)
            }
            .addOnFailureListener { e ->
                Log.d("ChatActivity", "Error retrieving user data: ${e.message}")
                currentUser = User() // Initialize with a default value
                displayUserData(name, nameTv, userImgView)
            }
        sendBtn.setOnClickListener {
            msgEt.text.let {
                if (it.isNotEmpty()) {
                    sendMessage(it.toString())
                    it.clear()
                }
            }
        }
    }

    private fun sendMessage(msg: String) {
        val id = getMessages(friendId).push().key   //uniqueKey
        checkNotNull(id) { "Cannot be null" }
        val msgMap = Message(msg, mCurrentUid, id)
        getMessages(friendId).child(id).setValue(msgMap).addOnSuccessListener {

        }
    }


    private fun displayUserData(name: String, nameTv: TextView, userImgView: ShapeableImageView) {
        nameTv.text = name
        if (image.isNotEmpty()) {
            Picasso.get().load(image).into(userImgView)
        }
    }

    private fun getMessages(friendId: String) = db.reference.child("messages/${getId(friendId)}")

    private fun getinbox(toUser: String, fromUser: String) =
        db.reference.child("chats/$toUser/$fromUser")

    private fun getId(friendId: String): String { //Id for the messages
        return if (friendId > mCurrentUid) {
            mCurrentUid + friendId
        } else {
            friendId + mCurrentUid
        }
    }

//    override fun onBackPressed(){
//        super.onBackPressed()
//        val intent = Intent(this, PeopleFragment::class.java)
//        startActivity(intent)
//    }

    companion object {
        fun createChatActivity(context: Context, id: String, name: String, image: String): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(UID, id)
            intent.putExtra(NAME, name)
            intent.putExtra(IMAGE, image)

            return intent
        }
    }
}