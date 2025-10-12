 //Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")  // <-- Aquí actualizamos el plugin
        //classpath("com.google.gms:google-services:4.4.2")  // <-- Tu línea existente para Firebase
    }
}

plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.0" apply false
    id("de.undercouch.download") version "4.1.2" apply false
    id("com.google.firebase.crashlytics") version "2.9.2" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
}
