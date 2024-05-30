plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services") version "4.4.1" apply false

}

android {
    namespace = "com.example.guardiango"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.guardiango"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")

    implementation ("com.google.android.libraries.places:places:3.4.0")

    implementation ("com.google.maps:google-maps-services:0.15.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.6")

    implementation ("com.google.code.gson:gson:2.8.6")

    implementation ("androidx.slidingpanelayout:slidingpanelayout:1.2.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}