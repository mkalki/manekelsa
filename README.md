# ManeKelsa

ManeKelsa is an Android application built using Kotlin and Jetpack Compose that helps users find nearby workers such as cleaners, electricians, plumbers, cooks, and other daily service providers.

The app supports Kannada language localization and uses Firebase Firestore for realtime worker data storage.

---

# Features

- Find nearby workers
- Add worker profiles
- Realtime Firebase Firestore integration
- Kannada language support
- Search workers by:
    - Name
    - Skill
    - City
- Worker profile images
- Direct call functionality
- Modern Jetpack Compose UI
- GPS location support
- Material 3 design

---

# Tech Stack

- Kotlin
- Jetpack Compose
- Firebase Firestore
- Navigation Compose
- Coil Image Loading
- Material 3
- Android Studio

---

# Project Structure

```text
MainActivity.kt
HomeScreen.kt
WorkerListScreen.kt
AddWorkerScreen.kt
SplashScreen.kt
Worker.kt
LocationUtils.kt
```

---

# Screens

## Home Screen
- Find Workers
- Add Worker

## Worker List Screen
- Displays all workers
- Search functionality
- Call workers directly

## Add Worker Screen
- Add worker details
- Upload profile image URL
- Save realtime data to Firebase

---

# Firebase Setup

1. Create Firebase project
2. Enable Firestore Database
3. Download `google-services.json`
4. Place it inside:

```text
app/
```

---

# Kannada Localization

The app supports Kannada using:

```text
res/values-kn/strings.xml
```

Android automatically switches language based on device language settings.

---

# Permissions

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

---

# Future Improvements

- Firebase Authentication
- Worker verification
- Gallery image upload
- Firebase Storage
- Nearby worker sorting
- Maps integration
- AI worker recommendations
- Worker booking system
- Dark mode improvements
- Advanced realtime GPS tracking improvements

---

# Author

Mohan R

---

# License

This project is for educational and learning purposes.