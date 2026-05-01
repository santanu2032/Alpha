<p align="center">
  <img src="Gemini_Generated_Image_v97d2tv97d2tv97d.png" alt="Alpha Banner" width="100%">
</p>


# Alpha: Offline Android Voice Recognition Module

A lightweight, production-ready sensory module for Android applications. This project provides a bridge between Google ML Kit Speech Recognition (Kotlin) and central application logic (Java).

## Features
- 100% Offline: Uses on-device ML models.
- Modular Design: Isolated "Sensory Department" architecture.
- Error Handling: 3-attempt recursive retry for silence/timeouts.
- Java Bridge: Clean interface for Java-based projects like JARVIS.

## How It Works
The project uses a Link class to act as a "Middle Manager." You simply pass a listener and wait for the text.

### Example for Java
Link sensoryDept = new Link();

sensoryDept.startAlphaEars(text -> {
    System.out.println("Result: " + text);
    if (text.startsWith("ERROR")) {
        // Handle failure
    } else {
        // Process command
    }
});

### Example for Kotlin
val link = Link()
link.startAlphaEars(object : Link.TaskListener {
    override fun onHeard(text: String) {
        println("User said: $text")
    }
})

## Credits & Author
- Lead Developer: [Santanu Sarkar](https://github.com/santanu2032)
- Project: Part of the JARVIS Ecosystem (Sensory Department).
- AI Collaborator: Gemini (Google) - Assisted with bridge logic and error-handling optimization.

## Special Thanks
- Google ML Kit Team: For the Alpha Speech Recognition SDK.
- Kotlin Coroutines Team: For background processing.
