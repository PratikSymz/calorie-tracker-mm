plugins {
    `android-library`
    `kotlin-android`
}

// Apply the root gradle config to this child gradle
apply(from = "$rootDir/compose-module.gradle")

// Define its namespace
android {
    namespace = "com.example.core_ui"
}