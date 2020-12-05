plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.dokka")
    id("com.cookpad.android.plugin.license-tools")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
    moduleName.set("app")
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "com.keygenqt.mylibrary"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets {
        getByName("main").let { data ->
            data.java.srcDir("src/main/kotlin")
            data.res.setSrcDirs(emptySet<String>())
            file("src/main/res").listFiles()!!.toList().forEach { dir ->
                data.res.srcDir(dir)
            }
        }
    }
}

dependencies {
    implementation("com.airbnb.android:lottie:3.5.0")
    implementation("com.github.arimorty:floatingsearchview:2.1.1")
}

// Glide
dependencies {
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
}

// RoundedImageView
dependencies {
    implementation("com.makeramen:roundedimageview:2.3.0")
}

// Material View Pager Dots Indicator
dependencies {
    implementation("com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2")
}

// material
dependencies {
    implementation("com.google.android.material:material:1.2.1")
}

// room
dependencies {
    implementation("androidx.room:room-runtime:${findProperty("room_version")}")
    implementation("androidx.room:room-ktx:${findProperty("room_version")}")
    kapt("androidx.room:room-compiler:${findProperty("room_version")}")
}

// security
dependencies {
    implementation("com.facebook.stetho:stetho:1.5.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha02")
    implementation("androidx.security:security-identity-credential:1.0.0-alpha01")
}

dependencies {
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
}

dependencies {
    implementation("androidx.navigation:navigation-fragment:2.3.1")
    implementation("androidx.navigation:navigation-ui:2.3.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
}

dependencies {
    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

dependencies {
    implementation("org.koin:koin-android:${findProperty("koin_version")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
}