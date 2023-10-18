package com.example.blinkchat.fragments

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkchat.R
import com.example.blinkchat.models.User
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

open class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User, onClick: (name: String, photo: String, id: String) -> Unit) {
        val countTv: TextView = itemView.findViewById(R.id.countTv)
        val timeTv: TextView = itemView.findViewById(R.id.timeTv)
        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val subtitleTv: TextView = itemView.findViewById(R.id.subTitleTv)
        val userImgView: ShapeableImageView = itemView.findViewById(R.id.userImgView)

        countTv.isVisible = false
        timeTv.isVisible = false
        titleTv.text = user.name
        subtitleTv.text = user.status

        Picasso.get()
            .load(user.thumbImage)
            .placeholder(R.mipmap.default_avatar)
            .error(R.mipmap.default_avatar)
            .into(userImgView)

        itemView.setOnClickListener {
            onClick.invoke(user.name, user.thumbImage, user.uid)
        }
    }
}
