plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // ✅ forma correta no .kts
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36 // ✅ use número direto (36 ainda não existe oficialmente)

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 27
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
        viewBinding = true
    }
}

dependencies {
    // Firebase BOM (centraliza versões)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Realtime Database
    implementation("com.google.firebase:firebase-database-ktx")

    // Core utilities
    implementation("com.google.firebase:firebase-common-ktx")

    // Outras dependências do Android (provavelmente já vieram do template)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
