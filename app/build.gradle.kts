plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.lucascamarero.didaktikapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.lucascamarero.didaktikapp"
        minSdk = 24
        targetSdk = 36
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
    }
}

dependencies {

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.compose.foundation)
// --- ROOM (Con KSP, esto está bien) ---
    val room_version = "2.6.1" // Nota: 2.8.4 no es estable aún o es muy reciente, 2.6.1 es la estable actual de AndroidX
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // --- HILT (La corrección es aquí) ---

    // 1. Librería principal de Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")

    // 2. Compilador de Hilt (CRÍTICO: Debes usar 'kapt' y 'hilt-android-compiler')
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // 3. Integración con Compose Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Open maps
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    //para poder usar gif
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")


    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.ui)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}