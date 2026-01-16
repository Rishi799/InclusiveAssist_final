# 📱 Inclusive Assistant

**Inclusive Assistant** is an all-in-one Android application designed to empower individuals with visual and hearing impairments. By leveraging smartphone sensors, cameras, and AI, this app provides essential tools to navigate the world independently and safely.

---

## 🚀 Features

### 👁️ For Visually Impaired (Blind Mode)
* **🔊 Voice Navigation:** Every button and action is spoken aloud using Text-to-Speech (TTS).
* **📷 Object Detection:** Identifies everyday objects using the camera and speaks their names.
* **📖 Text Reader:** Scans books, signs, or documents and reads the text aloud (OCR).
* **💵 Currency Recognition:** Identifies currency notes to help with financial transactions.
* **📍 Location Announcer:** Speaks the current GPS address (Street, City) with one tap.
* **💡 Light Detector:** Beeps when a light source is detected (useful for knowing if room lights are on/off).

### 🧏 For Hearing Impaired (Deaf Mode)
* **🗣️ Speech-to-Text (Two-Way):** Live captioning for conversations + Type-to-Speak for replying.
* **🎓 Classroom Mode:** Continuous listening mode for lectures, seminars, or meetings without screen timeout.
* **🔔 Sound Detector:** Listens for loud noises (alarms, shouts) and alerts via **Vibration** and **Red Screen Flash**.
* **📍 Visual Location:** Displays the current address in **Large Text** to show taxi drivers or police.
* **💬 Quick Messages:** Full-screen digital flashcards for instant communication (e.g., "I am Deaf", "Hospital", "Police").
* **🆘 Emergency SOS:** Activates a high-speed flashlight strobe and heavy vibration to attract attention.

---

## 🛠️ Technology Stack
* **Language:** Java
* **UI:** XML (Android Layouts)
* **IDE:** Android Studio
* **APIs & Libraries:**
    * `Android TextToSpeech (TTS)`
    * `Android SpeechRecognizer`
    * `Google Play Services (Location)`
    * `Camera2 API` (for Light Detection/SOS)
    * `MediaRecorder` (for Sound Detection)

---

## ⚙️ Permissions Required
To function correctly, the app requires the following permissions (requested at runtime):
* `CAMERA` (For object detection, light detection, SOS)
* `RECORD_AUDIO` (For speech-to-text, sound detection)
* `ACCESS_FINE_LOCATION` (For GPS location features)
* `INTERNET` (For Geocoding address lookup)
* `VIBRATE` (For haptic feedback alerts)
* `FLASHLIGHT` (For SOS signals)

---

## 📥 How to Run the Project

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/YourUsername/InclusiveAssistant.git](https://github.com/YourUsername/InclusiveAssistant.git)
    ```
2.  **Open in Android Studio:**
    * Open Android Studio.
    * Select **File > Open** and choose the project folder.
3.  **Sync Gradle:**
    * Wait for the project to build and download dependencies.
4.  **Run on Device:**
    * Connect your Android phone via USB (Debugging ON).
    * Click the green **Run ▶️** button.

> **Note:** This app works best on a **physical device** because it relies on hardware sensors (Camera, Mic, GPS, Vibrator) that may not function fully in an emulator.

---

## 🔮 Future Scope
* **Multi-Language Support:** Adding support for Spanish, French, Hindi, etc.
* **Haptic Music Player:** Converting music bass into vibration patterns.
* **Specific Sound AI:** Recognizing specific sounds like "Baby Crying" or "Doorbell" using Machine Learning.
* **Navigation:** Turn-by-turn walking directions for blind users.

---

## 🤝 Contributing
Contributions are welcome! If you have ideas to improve accessibility:
1.  Fork the project.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

---

## 📄 License
This project is open-source and available under the **MIT License**.
