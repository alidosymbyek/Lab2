package com.example.chatlibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlibrary.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.text
            binding.timestampText.text = dateFormat.format(Date(message.timestamp))

            if (message.isSent) {
                // Sent message - align to right
                binding.messageCard.setCardBackgroundColor(
                    binding.root.context.getColor(android.R.color.holo_blue_dark)
                )
                binding.messageCard.layoutParams = (binding.messageCard.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    marginEnd = 0
                }
                binding.root.setPadding(100, 0, 0, 0) // Add padding to push message to right
            } else {
                // Received message - align to left
                binding.messageCard.setCardBackgroundColor(
                    binding.root.context.getColor(android.R.color.darker_gray)
                )
                binding.messageCard.layoutParams = (binding.messageCard.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    marginEnd = 0
                }
                binding.root.setPadding(0, 0, 100, 0) // Add padding to push message to left
            }
        }
    }
} 