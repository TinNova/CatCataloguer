import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

val composeVersion = "1.6.3"
val lifecycleVersion = "2.7.0"
val retrofitVersion = "2.9.0"
val jUnitVersion = "5.8.2"
val mockkVersion = "1.13.9"
val okHttp = "4.11.0"
val hiltVersion = "2.50"
val roomVersion = "2.6.1"



android {
    namespace = "com.tinnovakovic.catcataloguer"
    compileSdk = 34

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    packagingOptions {
        resources.excludes += setOf(
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md"
        )
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.tinnovakovic.catcataloguer"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField(
            "String",
            "THE_CAT_API_KEY",
            "\"${properties.getProperty("THE_CAT_API_KEY")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-paging:2.5.1")


    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    //Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-unit:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
//    implementation("androidx.compose.compiler:compiler:1.5.9")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")

    //Jetpack Compose Material Design Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("io.coil-kt:coil-compose:2.5.0")

    //ViewModel For Jetpack Compose

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-compiler:$hiltVersion")

    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.compose.material3:material3:1.2.0")


    //Coroutines Core Package
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    //Coroutines Provides Dispatchers.Main and Logs Unhandled Exceptions
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    testImplementation("app.cash.turbine:turbine:1.0.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //OkHttp Http Client For Kotlin & Java
    implementation("com.squareup.okhttp3:okhttp:$okHttp")
    //OkHttp Logs HTTP Requests & Responses
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttp")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //// Testing Dependencies ////

    //Mockk Mocking For Kotlin
    testImplementation("io.mockk:mockk:$mockkVersion")
    //Mockk Mocking For Kotlin with Android
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")

    //Junit5 For Writing Tests In Junit5
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    //Junit5 Core Package
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    //Coroutines Provides Utilities For Testing Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test-jvm:1.8.0")

    //Robolectric For UnitTesting The Android Framework
    testImplementation("org.robolectric:robolectric:4.7.3")

}

kapt {
    correctErrorTypes = true
}