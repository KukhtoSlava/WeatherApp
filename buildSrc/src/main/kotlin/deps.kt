@file:Suppress("unused", "ClassName", "SpellCheckingInspection")

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

object versions {
    const val kotlin = "1.7.10"
    const val sqldelight = "1.5.3"
    const val pluginVersion = "7.3.0"
}

object appConfig {
    const val applicationId = "com.kukhtoslava.weatherapp.android"

    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.0"

    const val minSdkVersion = 23
    const val targetSdkVersion = 33

    private const val MAJOR = 0
    private const val MINOR = 0
    private const val PATCH = 1
    const val versionCode = MAJOR * 10000 + MINOR * 100 + PATCH
    const val versionName = "$MAJOR.$MINOR.$PATCH"
}

object deps {

    object coroutines {
        private const val version = "1.6.2"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object serialization {
        private const val version = "1.4.0"

        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
    }

    object androidx {
        private const val activityVersion = "1.6.0"
        private const val splashScreenVersion = "1.0.0"
        private const val constraintlayoutComposeVersion = "1.0.1"

        const val activity = "androidx.activity:activity-compose:$activityVersion"
        const val splashScreen = "androidx.core:core-splashscreen:$splashScreenVersion"
        const val constraintlayoutCompose = "androidx.constraintlayout:constraintlayout-compose:$constraintlayoutComposeVersion"
    }

    object ktor {
        private const val version = "2.1.2"

        const val core = "io.ktor:ktor-client-core:$version"
        const val clientJson = "io.ktor:ktor-client-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val okHttp = "io.ktor:ktor-client-okhttp:$version"
        const val darwin = "io.ktor:ktor-client-darwin:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"
        const val mock = "io.ktor:ktor-client-mock:$version"
        const val negotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val serializationKotlinXJson = "io.ktor:ktor-serialization-kotlinx-json:$version"
    }

    object compose {
        const val androidxComposeCompilerVersion = "1.3.0"
        private const val version = "1.2.1"
        private const val androidxComposeMaterial3Version = "1.0.0-beta03"

        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:$version"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
        const val material3 = "androidx.compose.material3:material3:$androidxComposeMaterial3Version"
        const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class:$androidxComposeMaterial3Version"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
        const val uiUtil = "androidx.compose.ui:ui-util:$version"
    }

    object lifecycle {
        private const val version = "2.6.0-alpha01"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version" // viewModelScope
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version" // lifecycleScope
        const val runtimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:$version" // lifecycleScope
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
        const val viewModelKtxCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
    }

    object sqlDelight {
        private const val version = "1.5.3"

        const val runtime = "com.squareup.sqldelight:runtime:$version"
        const val android = "com.squareup.sqldelight:android-driver:$version"
        const val native = "com.squareup.sqldelight:native-driver:$version"
    }

    object kodein {
        private const val kodeinVersion = "7.15.0"

        const val core = "org.kodein.di:kodein-di:$kodeinVersion"
    }

    object mvvm {
        private const val mokoMvvmVersion = "0.14.0"

        const val core = "dev.icerock.moko:mvvm-core:$mokoMvvmVersion"
        const val flow = "dev.icerock.moko:mvvm-flow:$mokoMvvmVersion"
        const val flowCompose = "dev.icerock.moko:mvvm-flow-compose:$mokoMvvmVersion"
    }

    object navigation {
        private const val version = "0.26.4-beta"

        const val navigation = "com.google.accompanist:accompanist-navigation-animation:$version"
    }

    object test {
        const val junit = "junit:junit:4.13.2"

        object androidx {
            const val core = "androidx.test:core-ktx:1.4.0"
            const val junit = "androidx.test.ext:junit-ktx:1.1.3"

            object espresso {
                const val core = "androidx.test.espresso:espresso-core:3.4.0"
            }
        }

        const val mockk = "io.mockk:mockk:1.12.4"
        const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${versions.kotlin}"
        const val turbine = "app.cash.turbine:turbine:0.8.0"
    }

    const val flowExt = "io.github.hoc081098:FlowExt:0.4.0"
    const val napier = "io.github.aakira:napier:2.6.1"
}

private typealias PDsS = PluginDependenciesSpec
private typealias PDS = PluginDependencySpec

inline val PDsS.androidApplication: PDS get() = id("com.android.application")
inline val PDsS.androidLib: PDS get() = id("com.android.library")
inline val PDsS.kotlinAndroid: PDS get() = id("kotlin-android")
inline val PDsS.kotlin: PDS get() = id("kotlin")
inline val PDsS.kotlinKapt: PDS get() = id("kotlin-kapt")
inline val PDsS.kotlinParcelize: PDS get() = id("kotlin-parcelize")
inline val PDsS.kotlinxSerialization: PDS get() = id("kotlinx-serialization")
inline val PDsS.kotlinMultiplatform: PDS get() = kotlin("multiplatform")
inline val PDsS.kotlinNativeCocoapods: PDS get() = kotlin("native.cocoapods")
inline val PDsS.daggerHiltAndroid: PDS get() = id("dagger.hilt.android.plugin")
inline val PDsS.sqldelight: PDS get() = id("com.squareup.sqldelight")

fun DependencyHandler.addUnitTest(testImplementation: Boolean = true) {
    val configName = if (testImplementation) "testImplementation" else "implementation"

    add(configName, deps.test.junit)
    add(configName, deps.test.mockk)
    add(configName, deps.test.kotlinJUnit)
    add(configName, deps.coroutines.test)
}

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

@Suppress("NOTHING_TO_INLINE")
object L {
    inline operator fun <T> get(vararg elements: T): List<T> = elements.asList()
}

@Suppress("NOTHING_TO_INLINE")
object M {
    inline operator fun <K, V> get(vararg elements: Pair<K, V>) = elements.toMap()
}
