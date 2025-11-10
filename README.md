# AppSelecaoTeste

An Android application designed for remote device management, specifically tailored for kiosk mode and device locking/wiping based on Firebase commands.

[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)]()
[![License: Unlicense](https://img.shields.io/badge/license-Unlicense-blue.svg)](http://unlicense.org/)

## Table of Contents

1.  [Description](#description)
2.  [Features](#features)
3.  [Tech Stack](#tech-stack)
4.  [Installation](#installation)
5.  [Usage](#usage)
6.  [Project Structure](#project-structure)
7.  [Contributing](#contributing)
8.  [License](#license)
9.  [Important Links](#important-links)
10. [Footer](#footer)

## Description

AppSelecaoTeste is an Android application built in Kotlin that allows for remote management of devices through Firebase Realtime Database. Its primary use case involves locking devices into kiosk mode, unlocking them, and even wiping data remotely based on commands received from Firebase. The application also integrates with the Device Policy Manager to enforce security policies such as locking the device. It appears to be designed for scenarios where devices need to be remotely controlled based on payment status or other conditions.

## Features

-   **Remote Command Execution**: Listens for `lock`, `unlock`, and `wipe` commands from Firebase Realtime Database.
-   **Kiosk Mode**: Locks the device into a single-app mode using Android's Lock Task feature.
-   **Device Locking**: Immediately locks the device screen.
-   **Data Wiping**: Remotely wipes all data from the device (requires Device Admin privileges).
-   **Boot Receiver**: Automatically restarts the command service after the device boots.
-   **Real-time Feedback**: Provides real-time feedback to Firebase on the status of command execution.
-   **Adherence to Payment Status**: Enables or disables kiosk mode based on payment status (adimplente/inadimplente).

## Tech Stack

-   **Kotlin**: Primary programming language.
-   **Android SDK**: For building the Android application.
-   **Firebase Realtime Database**: For remote command and control.
-   **Android Device Policy Manager**: For device administration and kiosk mode.

## Installation

1.  **Clone the Repository**:

    ```bash
    git clone https://github.com/IagodFarias/AppSelecaoTeste.git
    cd AppSelecaoTeste
    ```

2.  **Set up Firebase**:

    -   Create a new project in the [Firebase Console](https://console.firebase.google.com/).
    -   Add your Android app to the Firebase project.
    -   Download the `google-services.json` file and place it in the `app/` directory.

3.  **Configure Device Admin**:

    -   The app uses `MyDeviceAdminReceiver` to handle device admin policies. Ensure this receiver is properly declared in your `AndroidManifest.xml` and that the necessary policies are defined in `res/xml/device_admin_policies.xml`.

4.  **Grant Device Admin Permission**:

    -   The application requires Device Admin permission to lock the screen and wipe data.  Users will need to grant this permission manually.

5.  **Build the Application**:

    -   Open the project in Android Studio.
    -   Build the project to resolve dependencies and generate the APK.

    ```bash
    ./gradlew assembleDebug
    ```

6.  **Install the Application**:

    -   Install the generated APK on your Android device or emulator.

## Usage

1.  **Enable Device Admin**:

    -   After installing the app, you will be prompted to enable Device Admin privileges. This is required for the app to lock the screen and potentially wipe data.

2.  **Configure Firebase Realtime Database**:

    -   Set up your Firebase Realtime Database with a `device/command` node.
    -   The app listens for commands (`lock`, `unlock`, `wipe`) at this node.

3.  **Send Commands**:

    -   Use the Firebase Console or your own backend to send commands to the `device/command` node.
    -   For example, to lock the device, set `device/command` to `lock`.

4.  **Observe the Device**:

    -   The device will respond to the commands received from Firebase.  For instance, setting the command to `lock` will lock the device and enter kiosk mode.

### Use Cases

-   **Remote Device Management**: Manage a fleet of Android devices remotely, such as in a retail or enterprise environment.
-   **Kiosk Mode**: Set up devices in kiosk mode for dedicated purposes, such as information displays or point-of-sale systems.
-   **Payment Enforcement**: Lock or wipe devices based on payment status.

## Project Structure

```
AppSelecaoTeste/
├── .idea/                       # IntelliJ IDEA project settings
├── app/
│   ├── google-services.json    # Firebase configuration file
│   ├── proguard-rules.pro      # ProGuard rules for code obfuscation
│   ├── src/
│   │   ├── androidTest/          # Instrumented tests
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml # Android application manifest
│   │   │   ├── java/com/example/myapplication/
│   │   │   │   ├── BootReceiver.kt        # Broadcast receiver for boot events
│   │   │   │   ├── CommandService.kt      # Service for executing commands from Firebase
│   │   │   │   ├── MainActivity.kt        # Main activity for the application
│   │   │   │   └── MyDeviceAdminReceiver.kt # Device admin receiver
│   │   │   ├── res/                 # Resources (layouts, drawables, values, etc.)
│   │   │   └── ...
│   │   ├── test/                 # Unit tests
├── gradle/                     # Gradle wrapper files
├── gradle.properties           # Gradle properties
├── gradlew                     # Gradle wrapper script
├── settings.gradle.kts        # Settings for Gradle build
└── README.md                   # Project documentation
```

## Contributing

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and commit them with descriptive messages.
4.  Submit a pull request.

## License

This project is licensed under the terms of the [Unlicense](http://unlicense.org/).

## Important Links

-   [Repository URL](https://github.com/IagodFarias/AppSelecaoTeste)

## Footer

**AppSelecaoTeste** - [https://github.com/IagodFarias/AppSelecaoTeste](https://github.com/IagodFarias/AppSelecaoTeste) by [IagodFarias](https://github.com/IagodFarias). Feel free to fork, star, and open issues to contribute!


