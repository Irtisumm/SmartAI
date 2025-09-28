plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}
apply(plugin = "com.google.gms.google-services")

android {
    // ⚠ Use a stable SDK. If you don’t have API 36 preview installed,
    // set these to 35 (Android 15) to avoid build errors.
    namespace = "com.example.citypulseai"
    compileSdk = 36  // ← was 36

    defaultConfig {
        applicationId = "com.example.citypulseai"
        minSdk = 24
        targetSdk = 35  // ← was 36
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
        viewBinding = true
    }
}

dependencies {
    // existing
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ---- NEW: Needed for your API + UI code ----

    // Retrofit + Gson + OkHttp logging
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // RecyclerView (explicit, since you’re using it in the layout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CircleImageView (your header avatar)
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Lifecycle runtime (good practice when using coroutines in Fragments)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Firebase BoM and Analytics
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    // Google Sign-In for Firebase Auth
    implementation("com.google.android.gms:play-services-auth:21.0.0")
}