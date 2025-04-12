package com.example.chatlibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatlibrary.databinding.ActivityChatBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var webSocketManager: WebSocketManager
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWebSocket()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }
    }

    private fun setupWebSocket() {
        webSocketManager = WebSocketManager { message ->
            runOnUiThread {
                when {
                    message.startsWith("ERROR:") -> {
                        Toast.makeText(this@ChatActivity, message, Toast.LENGTH_LONG).show()
                        addMessage(Message(message, false))
                    }
                    message == "CONNECTED" -> {
                        Toast.makeText(this@ChatActivity, "Connected to PieSocket server", Toast.LENGTH_SHORT).show()
                        addMessage(Message("Connected to server", false))
                    }
                    message == "DISCONNECTED" -> {
                        Toast.makeText(this@ChatActivity, "Disconnected from server", Toast.LENGTH_SHORT).show()
                        addMessage(Message("Disconnected from server", false))
                    }
                    message == "SPECIAL_MESSAGE" -> {
                        Toast.makeText(this@ChatActivity, "Mathematical equation received!", Toast.LENGTH_SHORT).show()
                        addMessage(Message("Mathematical equation: 203 = θ × cb", false))
                    }
                    else -> addMessage(Message(message, false))
                }
            }
        }
        webSocketManager.connect()
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotEmpty()) {
                webSocketManager.sendMessage(message)
                addMessage(Message(message, true))
                binding.messageInput.text?.clear()
            }
        }
    }

    private fun addMessage(message: Message) {
        messages.add(message)
        messageAdapter.notifyItemInserted(messages.size - 1)
        binding.messagesRecyclerView.scrollToPosition(messages.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.disconnect()
    }
} 