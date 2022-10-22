plugins {
    androidApplication
    kotlinAndroid
    kotlinKapt
    kotlinParcelize
}

android {
    namespace = appConfig.applicationId
    compileSdk = appConfig.compileSdkVersion
    defaultConfig {
        applicationId = appConfig.applicationId
        minSdk = appConfig.minSdkVersion
        targetSdk = appConfig.targetSdkVersion
        versionCode = appConfig.versionCode
        versionName = appConfig.versionName
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = deps.compose.androidxComposeCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))

    // AndroidX
    implementation(deps.androidx.activity)
    implementation(deps.androidx.splashScreen)
    implementation(deps.androidx.constraintlayoutCompose)

    // Lifecycle
    implementation(deps.lifecycle.viewModelKtx)
    implementation(deps.lifecycle.runtimeKtx)
    implementation(deps.lifecycle.runtimeCompose)
    implementation(deps.lifecycle.viewModelKtxCompose)

    // Navigation animation
    implementation(deps.navigation.navigation)

    // Compose
    implementation(deps.compose.ui)
    implementation(deps.compose.foundation)
    implementation(deps.compose.foundationLayout)
    implementation(deps.compose.materialIconsExtended)
    implementation(deps.compose.material3)
    implementation(deps.compose.uiToolingPreview)
    implementation(deps.compose.uiUtil)
    implementation(deps.compose.runtime)
    debugImplementation(deps.compose.uiTooling)
}
