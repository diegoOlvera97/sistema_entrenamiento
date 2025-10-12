plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.sistemaentrenamientocorporalypreparacinfisica"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sistemaentrenamientocorporalypreparacinfisica"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    aaptOptions {
        noCompress ("tflite")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}


dependencies {

// Usar BOM para asegurar versiones compatibles
    implementation(platform("io.github.jan-tennert.supabase:bom:2.4.0"))

    // Módulos que necesitas
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    // Si vas a usar storage o realtime eventualmente:
    implementation("io.github.jan-tennert.supabase:storage-kt")
    // implementation("io.github.jan-tennert.supabase:realtime-kt")

    // Motor HTTP requerido
    implementation("io.ktor:ktor-client-okhttp:2.3.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")


    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")       //es lo ultimo que agregue
    //implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")      //es lo ultimo que agregue
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")


    val navVersion = "2.9.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Import the BoM for the Firebase platform
    //implementation(platform("com.google.firebase:firebase-bom:34.1.0"))
    //implementation("com.google.firebase:firebase-auth-ktx")


    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    //implementation("com.google.firebase:firebase-auth")
    //implementation("com.google.firebase:firebase-database")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")

    implementation("androidx.datastore:datastore-preferences:1.1.7")
    //implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")     //es lo ultimo que agregue


    //implementation ("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")      //es lo ultimo que agregue
    // For control over item selection of both touch and mouse driven selection
    implementation ("androidx.recyclerview:recyclerview-selection:1.2.0")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    //Librerias para el control de la camara
    val camerax_version = "1.4.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")

    // CameraX Camera2 extensions
    implementation ("androidx.camera:camera-camera2:${camerax_version}")

    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")

    // CameraX View class
    implementation ("androidx.camera:camera-view:$camerax_version")

    // WindowManager
    implementation ("androidx.window:window:1.4.0")

    // Unit testing
    testImplementation ("junit:junit:4.13.2")

    // Instrumented testing
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    // MediaPipe Library
    //implementation ("com.google.mediapipe:tasks-vision:latest.release")
    implementation("com.google.mediapipe:tasks-vision:0.10.14")      //es lo ultimo que agregue

    //Volley
    implementation("com.android.volley:volley:1.2.1")

    //Convertir json a dataclass
    implementation ("com.google.code.gson:gson:2.10.1")

    //Piccaso
    implementation ("com.squareup.picasso:picasso:2.8")

    //Gif animados
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    // Because RxAndroid releases are few and far between, it is recommended you also
    // explicitly depend on RxJava's latest version for bug fixes and new features.
    // (see https://github.com/ReactiveX/RxJava/releases for latest 2.x.x version)
    implementation ("io.reactivex.rxjava2:rxjava:2.1.1")


    // Pose detection with default models
    implementation ("com.google.mlkit:pose-detection:18.0.0-beta5")
    // Pose detection with accurate models
    implementation ("com.google.mlkit:pose-detection-accurate:18.0.0-beta5")

    //implementation ("org.tensorflow:tensorflow-lite:+")
    implementation("org.tensorflow:tensorflow-lite:2.14.0")    //es lo ultimo que agregue

}