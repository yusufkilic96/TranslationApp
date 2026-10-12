# TranslationApp

TranslationApp is an Android application built around PocketSphinx, an offline speech-recognition library. The project includes a native Android application and a separate PocketSphinx Android module.

The application requests microphone access, suggesting that speech input is part of its functionality.

## Features

- Speech input through the device microphone.
- Integration with PocketSphinx for speech recognition.
- Android application interface.
- Potential offline speech-recognition capabilities through PocketSphinx.

**Note:** Translation functionality, supported languages, and the precise offline capabilities should be verified against the application source and configuration.

## Tech Stack

- **Language:** Java
- **Platform:** Android
- **Build system:** Gradle
- **Android Gradle Plugin:** 7.1.2
- **Gradle wrapper:** 7.2
- **Compile SDK:** 29
- **Minimum SDK:** 22 (Android 5.1)
- **Target SDK:** 29
- **UI libraries:** AndroidX AppCompat, ConstraintLayout
- **Speech recognition:** PocketSphinx Android
- **Testing:** JUnit 4, AndroidX Test, Espresso

## Project Structure

```text
TranslationApp/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/example/translationapp/
│       │   │   └── MainActivity.java
│       │   └── AndroidManifest.xml
│       ├── test/
│       └── androidTest/
├── pocketsphinx-android-5prealpha-release/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

The repository contains two Gradle modules:

- `app`: The main Android application.
- `pocketsphinx-android-5prealpha-release`: The PocketSphinx Android library module.

## Requirements

- Android Studio compatible with Android Gradle Plugin 7.1.2.
- JDK 11 for the Gradle 7.2 build environment.
- Android SDK Platform 29.
- An Android emulator or physical Android device.
- Microphone access for speech-recognition features.

## Getting Started

Clone the repository:

```bash
git clone https://git.home.yusufkilic.dev/yusuf.kilic/TranslationApp.git
```

Open the cloned project in Android Studio and allow Gradle synchronization to finish.

Ensure the Android SDK and required build tools are installed, then run the application on an emulator or a physical device.

## Build

On Windows, build the debug APK from the repository root:

```powershell
.\gradlew.bat assembleDebug
```

The expected APK output path is:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Install the debug build on a connected Android device:

```powershell
.\gradlew.bat installDebug
```

List available Gradle tasks:

```powershell
.\gradlew.bat tasks
```

## Testing

Run unit tests:

```powershell
.\gradlew.bat test
```

Run instrumented tests on a connected device or emulator:

```powershell
.\gradlew.bat connectedAndroidTest
```

## Permissions

The Android manifest declares the following permissions:

- `RECORD_AUDIO`: Required for microphone-based speech input.
- `WRITE_EXTERNAL_STORAGE`: Declared by the application; verify whether it is still needed and how it is used.

On supported Android versions, microphone access may require runtime permission handling. Verify that the application requests permission before starting speech recognition.

## Speech Recognition and Language Support

PocketSphinx is included as a separate project module. Speech recognition generally requires suitable acoustic models, language models or grammars, and dictionary resources, depending on the configuration.

Check the PocketSphinx assets and application initialization code to document:

- Supported recognition languages.
- Whether recognition runs fully offline.
- How speech-recognition models are loaded.
- Whether recognized speech is translated or merely transcribed.
- How recognized text is displayed to the user.

## Troubleshooting

### Gradle synchronization fails

Confirm that Android Studio uses a compatible JDK and that Android SDK Platform 29 is installed.

Check the Gradle environment:

```powershell
.\gradlew.bat --version
```

### Speech recognition does not start

- Grant microphone permission.
- Verify that the device microphone is working.
- Confirm that PocketSphinx assets and model files are available.
- Check Logcat for initialization or recognition errors.
