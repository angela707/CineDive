import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.parcelize.get().pluginId)
}

android {
    namespace = "com.adimovska.cinedive"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.adimovska.cinedive"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = File(rootDir, "apikeys.properties")
        val apiKey = if (localProperties.exists()) {
            localProperties.inputStream().use { input ->
                Properties().apply { load(input) }.getProperty("api.token")
            }
        } else {
            null
        }
        buildConfigField("String", "API_TOKEN", "\"${apiKey ?: ""}\"")

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

    implementation(libs.material.icons.extended)
    implementation(libs.compose.coil)
    implementation(libs.compose.destinations.core)
    implementation(libs.androidx.multidex)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.testing)

    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)
    implementation(libs.logging.interceptor)

    implementation(libs.bundles.paging)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}