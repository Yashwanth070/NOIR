package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL_NAME = "gemini-3.5-flash"
    
    // OkHttpClient with 60-second timeouts per security/stability rules
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Sends a chat prompt to Gemini. Supports message history for contextual chatbot assistants.
     * @param prompt Current message from the user
     * @param history Previous messages formatted as pairs of ("user" or "model", messageText)
     */
    suspend fun chatWithBot(prompt: String, history: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "API Key is missing or default!")
            return@withContext "API Configuration Notice: Gemini API Key is missing. Please enter your API Key in the Secrets panel in AI Studio with the variable name 'GEMINI_API_KEY' of your project config to activate the real AI assistant. (Running in Premium Simulator Mode)"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"

        try {
            // Build direct JSON body matching the Gemini API schema perfectly
            val requestJson = JSONObject()
            val contentsArray = JSONArray()

            // Include history
            for (turn in history) {
                val turnObj = JSONObject()
                turnObj.put("role", if (turn.first == "user") "user" else "model")
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", turn.second)
                partsArray.put(partObj)
                turnObj.put("parts", partsArray)
                contentsArray.put(turnObj)
            }

            // Include current user prompt
            val currentUserTurn = JSONObject()
            currentUserTurn.put("role", "user")
            val currentParts = JSONArray()
            val currentPart = JSONObject()
            currentPart.put("text", "You are the smart built-in AI chatbot assistant of NOIR, an ultra-premium real-time secure messaging app built by a billion-dollar startup. Provide short, concise, engaging, and smart responses to developers and users. Key user query: $prompt")
            currentParts.put(currentPart)
            currentUserTurn.put("parts", currentParts)
            contentsArray.put(currentUserTurn)

            requestJson.put("contents", contentsArray)

            // Dynamic configuration
            val generationConfig = JSONObject()
            generationConfig.put("temperature", 0.7)
            generationConfig.put("topP", 0.95)
            requestJson.put("generationConfig", generationConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errCode = response.code
                    val errMsg = response.body?.string() ?: ""
                    Log.e(TAG, "Unsuccessful response: $errCode -> $errMsg")
                    return@withContext "Connection Issue: API returned code $errCode. Please ensure your Gemini billing status or API limits are configured."
                }

                val responseBody = response.body?.string() ?: return@withContext "Error: Received empty response from intelligence server."
                val responseJson = JSONObject(responseBody)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    if (contentObj != null) {
                        val parts = contentObj.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "Let me think about that...")
                        }
                    }
                }
                return@withContext "I received your message, but the model did not generate output."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network request exception", e)
            return@withContext "Offline System Trace: ${e.localizedMessage ?: "Network error"}. The chatbot simulator is working offline."
        }
    }
}
