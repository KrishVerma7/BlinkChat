package com.example.blinkchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class SignUpActivity : AppCompatActivity() {
    private val storage by lazy {
        FirebaseStorage.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database by lazy {
        FirebaseFirestore.getInstance()
    }
    lateinit var userImgView: ShapeableImageView
    lateinit var downloadUrl: String
    lateinit var nextBtn: Button
    lateinit var nameEt: EditText

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userImgView = findViewById(R.id.userImgView)
        nextBtn = findViewById(R.id.nextBtn)
        nameEt = findViewById(R.id.nameEt)

        userImgView.setOnClickListener {
            checkPermissionForImage()
        }

        nextBtn.setOnClickListener {
            nextBtn.isEnabled = false
            val name = nameEt.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!::downloadUrl.isInitialized) {
                Toast.makeText(this, "Image cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(name, downloadUrl, downloadUrl, auth.uid!!)
                database.collection("users").document(auth.uid!!).set(user).addOnCompleteListener {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }.addOnFailureListener {
                    nextBtn.isEnabled = true
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionForImage() {
        if ((checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            val permissionWrite = arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)

            requestPermissions(
                permission,
                1001
            )   //GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            requestPermissions(
                permissionWrite,
                1002
            )   //GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            1000
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            data?.data?.let {
                userImgView.setImageURI(it)
                uploadImage(it)
            }
        }

    }

    private fun uploadImage(it: Uri) {
        nextBtn = findViewById(R.id.nextBtn)

        nextBtn.isEnabled = false

        // Get a reference to the Firebase Storage location where the image will be stored.
        val ref = storage.reference.child("uploads/" + auth.uid.toString())

        // Create an upload task for the specified Uri (image file).
        val uploadTask = ref.putFile(it)

        // Continue with a task when the upload task completes.
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                // If the upload task is not successful, throw an exception to handle it.
                task.exception.let {
                    throw it!!
                }
            }
            // If the upload is successful, return a Task that resolves to the download URL of the uploaded file.
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            nextBtn.isEnabled = true
            if (task.isSuccessful) {
                // If the download URL retrieval task is successful, store the URL.
                downloadUrl = task.result.toString()
                Log.i("URL", "downloadUrl: $downloadUrl")
            } else {
                nextBtn.isEnabled = true
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Upload image Failed", Toast.LENGTH_SHORT).show()
        }
    }
}