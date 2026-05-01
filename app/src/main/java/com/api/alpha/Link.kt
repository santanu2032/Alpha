package com.api.alpha

import com.google.mlkit.genai.common.audio.AudioSource


class Link {
    val translator = VoiceTranslator()

    // The other class creates the voice object
    val myVoiceInput = AudioSource.fromMic()


    public interface TaskListener{           //use the interface logic to get the string to your logic or core class
        fun onHeard(text: String)
    }


    fun startAlphaEars(task: TaskListener) {
// Pass the object into the function
        translator.convertToString(myVoiceInput, object : VoiceTranslator.ResultListener {
            override fun onTextReady(resultString: String) {
                // You get your final string right here
                println(resultString)//only for debugging
                task.onHeard(resultString)
            }

            override fun onError(e: Exception) {
                println(e.message)
                task.onHeard("error"+e.message)
            }
        })
    }
    fun destroyEars(){
        translator.close()
    }
}