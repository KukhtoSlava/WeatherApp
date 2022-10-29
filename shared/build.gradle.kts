plugins {
    kotlinMultiplatform
    kotlinNativeCocoapods
    androidLib
    kotlinxSerialization
    kotlinKapt
    sqldelight
}

version = appConfig.versionName

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Weather application"
        homepage = "https://openweathermap.org"
        version = "1.0"
        ios.deploymentTarget = "15"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "MultiPlatformLibrary"
            isStatic = false
            export(deps.mvvm.core)
            export(deps.mvvm.flow)
        }
    }
    
    sourceSets {
        all {
            languageSettings.run {
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":flowredux"))

                // Flow, Coroutines
                api(deps.coroutines.core)
                implementation(deps.flowExt)

                // Serialization
                implementation(deps.serialization.core)
                implementation(deps.serialization.json)

                // SqlDelight
                implementation(deps.sqlDelight.runtime)

                // Ktor
                implementation(deps.ktor.core)
                implementation(deps.ktor.clientJson)
                implementation(deps.ktor.serializationKotlinXJson)
                implementation(deps.ktor.negotiation)
                implementation(deps.ktor.logging)
                implementation(deps.ktor.serialization)

                // Logger
                api(deps.napier)

                // Kodein
                api(deps.kodein.core)

                // MVVM
                api(deps.mvvm.core)
                api(deps.mvvm.flow)

                // Permissions
                implementation(deps.permissions.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(deps.ktor.okHttp)
                implementation(deps.sqlDelight.android)
                api(deps.mvvm.flowCompose)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(deps.sqlDelight.native)
                implementation(deps.ktor.darwin)
                implementation(deps.ktor.core)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = appConfig.applicationId
    compileSdk = appConfig.compileSdkVersion
    defaultConfig {
        minSdk = appConfig.minSdkVersion
        targetSdk = appConfig.targetSdkVersion
    }
}

sqldelight {
    database("CityDBDatabase") {
        packageName = "com.kukhtoslava.weatherapp"
        sourceFolders = listOf("sqldelight")
    }
}
