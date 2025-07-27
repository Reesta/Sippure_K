import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Ensure this plugin is applied
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.plugin.parcelize") // <--- ADD THIS LINE FOR KOTLIN PARCELIZE
}

android {
    namespace = "com.example.sippure"
    compileSdk = 35 // Current compileSdk

    defaultConfig {
        applicationId = "com.example.sippure"
        minSdk = 24
        targetSdk = 35 // Current targetSdk
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
    composeOptions {
        // IMPORTANT: This version must match the Kotlin version used by your Compose BOM
        // For compose-bom:2024.04.00, a common compatible version is "1.5.1" or "1.5.10"
        kotlinCompilerExtensionVersion = "1.5.10" // Recommended for compatibility with recent Compose BOMs and Kotlin
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials) - manages compatible versions for Compose libraries
    implementation(platform(libs.androidx.compose.bom)) // Assuming libs.androidx.compose.bom points to "androidx.compose:compose-bom:2024.04.00" or similar

    // Compose UI and Graphics
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Compose Material 3 (already present, but ensures it's pulled from BOM)
    implementation(libs.androidx.material3) // This should be sufficient for Material 3 components

    // ADD THIS LINE FOR EXTENDED MATERIAL ICONS (e.g., Icons.Filled, Icons.Outlined)
    implementation("androidx.compose.material:material-icons-extended")

    // LiveData integration for Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0") // Keep your existing version

    // Third-party image loading libraries (keep existing)
    implementation("com.cloudinary:cloudinary-android:2.1.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Firebase (keep existing)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)


    // Testing dependencies (keep existing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}