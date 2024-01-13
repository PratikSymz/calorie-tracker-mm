plugins {
    `android-library`
    `kotlin-android`
}

// Apply the root gradle config to this child gradle
apply(from = "$rootDir/base-module.gradle")

// Define its namespace
android {
    namespace = "com.pratiksymz.core"
}