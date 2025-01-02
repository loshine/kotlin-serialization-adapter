// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }

    dependencies {
        // classpath("io.github.loshine:json-compiler-gradle-plugin:0.0.1-SNAPSHOT")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}
