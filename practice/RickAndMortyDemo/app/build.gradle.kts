plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "hu.paulolajos.rickandmortydemo"
    compileSdk = 33

    defaultConfig {
        applicationId = "hu.paulolajos.rickandmortydemo"
        minSdk = 28
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    hilt {
        enableAggregatingTask = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Navigation
    // no update to 2.7 - need 34 sdk
    val nav_version = "2.6.0"
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Room
    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    //Lifecycle
    val lifecycle_version = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}