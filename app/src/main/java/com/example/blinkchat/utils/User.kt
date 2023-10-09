package com.example.blinkchat.utils

import com.google.firebase.firestore.FieldValue

data class User(
    val name: String,
    val imageUrl: String,
    val thumbImage: String,
    val uid: String,
    val deviceToken: String,
    val status: String,
    val onlineStatus: FieldValue,

    ) {
    //*IMPORTANT* when we make a data class for firebase, we need to implement an empty constructor,else code will not work
    /** Empty [Constructor] for firebase */
    constructor() : this("", "", "", "","","",FieldValue.serverTimestamp())

    constructor(name: String, imageUrl: String, thumbImage: String, uid: String) : this(
        name,
        imageUrl,
        thumbImage,
        uid,
        "",
        "Hey there, I'm using BlinkChat",
        FieldValue.serverTimestamp()


    )

}