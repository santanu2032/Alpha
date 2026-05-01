package com.api.alpha

import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.audio.AudioSource
import com.google.mlkit.genai.speechrecognition.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import java.util.Locale

class VoiceTranslator {

    private val recognizer: SpeechRecognizer
    // 1. Give the background worker a name so we can control it
    private var listeningJob: Job? = null

    interface ResultListener {
        fun onTextReady(resultString: String)
        fun onError(e: Exception)
    }

    init {
        val options = speechRecognizerOptions {
            preferredMode = SpeechRecognizerOptions.Mode.MODE_BASIC
            locale = Locale.ENGLISH
        }
        recognizer = SpeechRecognition.getClient(options)
    }

    fun convertToString(voiceObject: AudioSource, listener: ResultListener, retriesLeft: Int = 3) {
        val request = speechRecognizerRequest {
            audioSource = voiceObject
        }

        // 2. Assign the background worker to our variable
        listeningJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val status = recognizer.checkStatus()

                if (status == FeatureStatus.DOWNLOADABLE) {
                    println("ALPHA_STATUS: Brain missing! Downloading ML Kit Model... please wait.")
                    recognizer.download().collect { downloadProgress ->
                        println("ALPHA_STATUS: Download update -> $downloadProgress")
                    }
                }

                println("ALPHA_STATUS: Mic Open! (Retries left: $retriesLeft)")
                recognizer.startRecognition(request).collect { response ->
                    println("ALPHA_STATUS: $response")

                    if (response is SpeechRecognizerResponse.FinalTextResponse) {
                        listener.onTextReady(resultString = response.text)
                    }
                }

                // 4. Catch the intentional cancellation so it doesn't trigger a retry
            }

            catch (e: Exception) {
                val errorMessage = e.message ?: ""

                if ((errorMessage.contains("INVALID_REQUEST") || errorMessage.contains("timeout", ignoreCase = true)) && retriesLeft > 0) {
                    println("ALPHA_STATUS: Heard silence. Restarting mic... ($retriesLeft tries remaining)")
                    convertToString(voiceObject, listener, retriesLeft - 1)
                } else {
                    println("ALPHA_STATUS: No voice detected. Shutting down ears.")
                    listener.onError(e)
                }
            }
        }
    }

    fun close() {
        recognizer.close()
    }
}