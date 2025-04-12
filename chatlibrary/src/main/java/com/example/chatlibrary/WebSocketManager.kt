package com.example.chatlibrary

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketManager(private val messageCallback: (String) -> Unit) {
    private var webSocketClient: WebSocketClient? = null
    private val serverUrl = "wss://s14445.nyc1.piesocket.com/v3/1?api_key=47cq3Gt48D4HKezHOqFl8GjRFjCDHrT2noL9ZxUF&notify_self=1"

    private fun createWebSocketClient(): WebSocketClient {
        return object : WebSocketClient(URI(serverUrl)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("WebSocket", "Connected")
                messageCallback("Connected to chat server")
            }

            override fun onMessage(message: String?) {
                Log.d("WebSocket", "Received message: $message")
                message?.let {
                    if (it == "$203=\\theta\\times cb") {
                        messageCallback("Mathematical equation: θ × cb = 203")
                    } else {
                        messageCallback(it)
                    }
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d("WebSocket", "Connection closed. Code: $code, Reason: $reason")
                messageCallback("Disconnected from chat server")
                webSocketClient = null
            }

            override fun onError(ex: Exception?) {
                Log.e("WebSocket", "Error: ${ex?.message}")
                messageCallback("Error: ${ex?.message}")
                webSocketClient = null
            }
        }
    }

    fun connect() {
        try {
            disconnect() // Close any existing connection
            Log.d("WebSocket", "Creating new connection to $serverUrl")
            webSocketClient = createWebSocketClient()
            webSocketClient?.connect()
        } catch (e: Exception) {
            Log.e("WebSocket", "Connection error: ${e.message}")
            messageCallback("Connection error: ${e.message}")
            webSocketClient = null
        }
    }

    fun sendMessage(message: String) {
        try {
            if (webSocketClient?.isOpen == true) {
                webSocketClient?.send(message)
                Log.d("WebSocket", "Message sent: $message")
            } else {
                Log.d("WebSocket", "WebSocket is not connected, attempting to connect...")
                connect()
                // Queue the message to be sent after connection
                webSocketClient?.let { client ->
                    Thread {
                        var attempts = 0
                        while (attempts < 5 && !client.isOpen) {
                            Thread.sleep(1000)
                            attempts++
                        }
                        if (client.isOpen) {
                            client.send(message)
                            Log.d("WebSocket", "Message sent after reconnection: $message")
                        } else {
                            messageCallback("Failed to send message: Connection timeout")
                        }
                    }.start()
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Send error: ${e.message}")
            messageCallback("Send error: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            webSocketClient?.let { client ->
                if (client.isOpen) {
                    client.close()
                }
            }
            webSocketClient = null
            Log.d("WebSocket", "Disconnected")
        } catch (e: Exception) {
            Log.e("WebSocket", "Disconnect error: ${e.message}")
        }
    }
} 