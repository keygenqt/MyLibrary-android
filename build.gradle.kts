buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("kotlin_version")}")
        classpath("org.koin:koin-gradle-plugin:${findProperty("koin_version")}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${findProperty("dokka_version")}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.2")
        classpath("gradle.plugin.com.cookpad.android.plugin:plugin:1.2.5")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.4.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://artifactory.keygenqt.com/artifactory/open-source")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}