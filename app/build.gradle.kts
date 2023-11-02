plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.blinkchat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.blinkchat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.9.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.emoji2:emoji2-emojipicker:1.4.0")
    implementation("com.google.firebase:firebase-firestore:24.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")

    implementation("com.hbb20:ccp:2.5.1")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation("com.google.android.flexbox:flexbox:3.0.0")

//    val emoji2_version = "1.4.0"
//
//    implementation("androidx.emoji2:emoji2:$emoji2_version")
//    implementation("androidx.emoji2:emoji2-views:$emoji2_version")
//    implementation("androidx.emoji2:emoji2-views-helper:$emoji2_version")
//
//    implementation("androidx.emoji:emoji:28.0.0")
//
//    implementation ("com.vanniktech:emoji-material:0.18.0-SNAPSHOT")
//    implementation ("com.hbb20:ccp:2.5.1")
//    implementation ("com.vanniktech:emoji-google:0.18.0")

}