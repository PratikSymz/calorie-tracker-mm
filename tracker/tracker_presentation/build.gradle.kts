plugins {
    `android-library`
    `kotlin-android`
}

// Apply the root gradle config to this child gradle
apply(from = "$rootDir/compose-module.gradle")

// Define its namespace
android {
    namespace = "com.example.tracker_presentation"
}

// Access dependencies from 'core' module
dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.trackerDomain))

    implementation(Coil.coilCompose)
}