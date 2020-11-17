buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("kotlin_version")}")
        classpath("org.koin:koin-gradle-plugin:${findProperty("koin_version")}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${findProperty("dokka_version")}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.1")
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