plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.locationReminder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.locationReminder"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation Compose dependency
    implementation (libs.androidx.navigation.compose)
    implementation (libs.navigation.compose)

    // Hilt Dependency Injection
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.android )
    kapt (libs.hilt.android.compiler)
    // Coil Compose dependency for loading images in Compose
    implementation (libs.coil.compose)
    // ConstraintLayout for Jetpack Compose
    implementation (libs.androidx.constraintlayout.compose)

    // Retrofit core library
    implementation (libs.retrofit)

    // Retrofit Gson converter for JSON parsing
    implementation (libs.converter.gson)

    // OkHttp for logging
    implementation (libs.logging.interceptor)

    // Coroutine support
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)



    // Google Maps and Location
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.places)

    // Material Design
    implementation(libs.material)

    // Room Database
    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.7.1")
    implementation(libs.androidx.room.ktx)


    implementation(libs.androidx.runtime.livedata)

    implementation (libs.androidx.lifecycle.process)



}