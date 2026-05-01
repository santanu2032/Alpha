package com.api.alpha

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // 1. Hire the Middle Manager (Create the Link object)
    private val sensoryLink = Link()

    // 2. Set up the "Legal Department" to ask for Microphone permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted! Give the order to start listening.
            Log.d("Alpha", "Permission granted. Starting ears...")
            sensoryLink.startAlphaEars(object : Link.TaskListener {
                override fun onHeard(text: String) {
                    // MainActivity successfully caught the string!
                    println("ALPHA_HEARD: $text")
                }
            })
        } else {
            // Permission denied.
            Toast.makeText(this, "Microphone permission is required!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Link to your layout XML file (if you have one)
        setContentView(R.layout.activity_main)

        // 3. As soon as the app opens, check permissions and start
        checkPermissionAndStart()
    }

    private fun checkPermissionAndStart() {
        // Check if we already have permission from a previous launch
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {

            // We have permission. Give the order to start!
            Log.d("Alpha", "Mic ready. Starting ears...")
            sensoryLink.startAlphaEars(object : Link.TaskListener {
                override fun onHeard(text: String) {
                    println("ALPHA_HEARD: $text")
                }
            })

        } else {
            // Ask the user for permission (This pops up the Android Allow/Deny dialog)
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        sensoryLink.destroyEars()
    }
}


