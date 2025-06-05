# Bengali Alphabet Learning App

This is a complete Android Studio project for a Bengali alphabet learning app that teaches users how to write Bengali characters through interactive tracing exercises.

## Features

- Touch gesture-based alphabet tracing with real-time feedback
- Progressive filling of letters as users trace correctly
- Guidance system with dotted lines and directional arrows after failed attempts
- Audio pronunciation support via the microphone icon
- Simple navigation with "Next" button to progress through characters

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture pattern and is organized as follows:

- **Model**: Data classes for Bengali characters and stroke paths
- **Views**: Custom views for tracing and guidance overlays
- **Manager**: Guidance system for monitoring tracing attempts and providing help

## Getting Started

1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the downloaded project directory and select it
4. Wait for Gradle sync to complete
5. Run the app on an emulator or physical device

## Requirements

- Android Studio Arctic Fox (2020.3.1) or newer
- Kotlin 1.5.0 or newer
- Android SDK 21 or higher
- Gradle 7.0 or newer

## Usage

The app provides an intuitive interface for learning to write Bengali characters:

1. Each screen displays a Bengali character to trace
2. Use your finger to trace the character following its shape
3. The app provides visual feedback as you trace correctly
4. After multiple failed attempts, guidance with dots and arrows appears
5. Once successfully completed, tap "Next" to proceed to the next character

## Customization

The app can be extended by:

- Adding more Bengali characters to the database
- Customizing the visual appearance through resources
- Adjusting the difficulty level by modifying tolerance parameters

## License

This project is provided as an educational tool and is licensed under the MIT License.
