plugins {
    id("com.android.application").version(versions.pluginVersion).apply(false)
    id("com.android.library").version(versions.pluginVersion).apply(false)
    kotlin("android").version(versions.kotlin).apply(false)
    kotlin("multiplatform").version(versions.kotlin).apply(false)
    id("org.jetbrains.kotlin.jvm").version(versions.kotlin).apply(false)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}")
        classpath(kotlin("serialization", version = versions.kotlin))
        classpath("com.squareup.sqldelight:gradle-plugin:${versions.sqldelight}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
