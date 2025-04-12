# Android Chat Library

A simple Android chat library that provides a WebSocket-based chat interface.

## Features

- Modern chat UI with distinct sender and receiver message styles
- Real-time WebSocket communication
- Support for special message handling (203 = 0xcb)
- Easy integration with a single method call

## Installation

Add the following to your project's `build.gradle.kts`:

```kotlin
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("com.example:chatlibrary:1.0.0")
}
```

## Usage

To start the chat interface, simply call:

```kotlin
ChatLibrary.start(context)
```

## Requirements

- Android API level 24 or higher
- Internet permission in your app's manifest

## Special Message Handling

The library handles the special message "203 = 0xcb" by displaying a predefined message instead of the raw data.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 