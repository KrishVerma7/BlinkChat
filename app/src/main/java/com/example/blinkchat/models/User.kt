package com.example.blinkchat.models

data class User(
    val deviceToken: String,
    val imageUrl: String,
    val name: String,
    val online: Boolean,
    val status: String,
    val thumbImage: String,
    val uid: String,

    ) {
    //*IMPORTANT* when we make a data class for firebase, we need to implement an empty constructor,else code will not work
    /** Empty [Constructor] for firebase */
    constructor() : this("", "", "", false, "", "", "")

    constructor(imageUrl: String, name: String, thumbImage: String, uid: String) :
            this(
                "",
                imageUrl,
                name,
                online = false,
                status = "Hey There, I am using BlinkChat",
                thumbImage,
                uid = uid
            )
}