plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.clase.engenios_ortega_manuel_prc6"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.clase.engenios_ortega_manuel_prc6"
        minSdk = 23
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("androidx.media3:media3-exoplayer:1.0.2")
    implementation ("androidx.media3:media3-ui:1.0.2")
    implementation ("androidx.media:media:1.6.0")
    implementation ("androidx.preference:preference:1.2.0")
}