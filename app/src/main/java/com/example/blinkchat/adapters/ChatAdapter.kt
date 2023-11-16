package com.example.blinkchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkchat.R
import com.example.blinkchat.models.ChatEvent
import com.example.blinkchat.models.DateHeader
import com.example.blinkchat.models.Message
import com.example.blinkchat.utils.formatAsTime

class ChatAdapter(private val list: MutableList<ChatEvent>, private val mCurrentUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflate = {layout:Int->
            LayoutInflater.from(parent.context).inflate(layout,parent,false)
        }

        return when(viewType){
            TEXT_MESSAGE_RECEIVED->{
                MessageViewHolder(inflate(R.layout.list_item_chat_recv_message))
            }
            TEXT_MESSAGE_SENT->{
                MessageViewHolder(inflate(R.layout.list_item_chat_sent_message))
            }
            DATE_HEADER->{
                DateViewHolder(inflate(R.layout.list_item_date_header))
            }
            else->MessageViewHolder(inflate(R.layout.list_item_chat_recv_message))
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(val item = list[position]){
            is DateHeader -> {
                val dateViewHolder = holder as DateViewHolder
                dateViewHolder.dateTextView.text = item.date
            }
            is Message ->{
                holder.itemView.apply{
                   val messageViewHolder = holder as MessageViewHolder
                    messageViewHolder.content.text = item.msg
                    val timeViewHolder = holder as MessageViewHolder
                    timeViewHolder.time.text = item.sentAt.formatAsTime()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(val event = list[position]){
            is Message ->{
                if(event.senderId == mCurrentUid){
                    TEXT_MESSAGE_SENT
                }else{
                    TEXT_MESSAGE_RECEIVED
                }
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val content: TextView = view.findViewById(R.id.content)
        val time: TextView = view.findViewById(R.id.time)
    }


    companion object {
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
    }
}