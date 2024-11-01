plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.wordseek"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wordseek"
        minSdk = 26
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
    val room_version= "2.6.1"
    //room database
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.5.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    //volley
    implementation("com.android.volley:volley:1.2.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.preference:preference-ktx:1.1.1")

    testImplementation("org.mockito:mockito-core:3.12.4")
    implementation("org.mockito:mockito-android:5.14.2")
    androidTestImplementation("org.mockito:mockito-android:3.12.4")
    implementation("androidx.tracing:tracing:1.1.0")

    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
}