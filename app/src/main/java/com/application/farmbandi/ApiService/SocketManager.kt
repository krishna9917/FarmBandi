package com.application.farmbandi.ApiService
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

object SocketManager {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun startWebSocket(url: String, listener: WebSocketListener) {
        if (webSocket == null) {
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer "+ MyLocalMemory.getString(MyApp.MySingleton.USER_TOKEN))
                .build()

            webSocket = client.newWebSocket(request, listener)
        }
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "Closing WebSocket")
        webSocket = null
    }
}