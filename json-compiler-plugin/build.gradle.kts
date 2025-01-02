import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.google.devtools.ksp)
    `maven-publish`
}

java {
    // sourceCompatibility = JavaVersion.VERSION_17
    // targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

group = "io.github.loshine"
version = "0.0.1-SNAPSHOT"

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                val component = components.find {
                    it.name == "java" || it.name == "release"
                }
                from(component)
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

dependencies {
    ksp(libs.auto.service.ksp)
    implementation(libs.auto.service.annotations)

    compileOnly(libs.kotlin.compiler.embeddable)
}
