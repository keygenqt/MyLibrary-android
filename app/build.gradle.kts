plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.dokka")
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
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

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
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.6.0")
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${findProperty("kotlin_version")}")
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
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

dependencies {
    implementation("org.koin:koin-android:${findProperty("koin_version")}")
}

dependencies {
    implementation("com.j256.ormlite:ormlite-android:5.1")
    kapt("com.keygenqt.artifactory:gen-ormlite-android:1.0.1")
}

