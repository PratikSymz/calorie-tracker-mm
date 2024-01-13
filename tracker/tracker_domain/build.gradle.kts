import org.jetbrains.kotlin.config.LanguageFeature

plugins {
    `android-library`
    `kotlin-android`
}

// Apply the root gradle config to this child gradle
apply(from = "$rootDir/base-module.gradle")

// Define its namespace
android {
    namespace = "com.example.tracker_domain"
}

// Access dependencies from 'core' module
dependencies {
    implementation(project(Modules.core))
    implementation(Coroutines.coroutines)
}