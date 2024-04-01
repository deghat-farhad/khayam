plugins {
    id(Plugins.ksp)
    id(Plugins.application)
    id(Plugins.hiltAndroid)
    id(Plugins.jetbrainsKotlinAndroid)
    id(Plugins.googleService)
    id(Plugins.crashlytics)
}

android {
    compileSdk = ConfigurationData.compileSdk
    namespace = ConfigurationData.applicationId

    defaultConfig {
        applicationId = ConfigurationData.applicationId
        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk
        versionCode = ConfigurationData.versionCode
        versionName = ConfigurationData.versionName
        vectorDrawables.useSupportLibrary = ConfigurationData.useSupportLibrary

        compileOptions {
            sourceCompatibility = ConfigurationData.javaVersion
            targetCompatibility = ConfigurationData.javaVersion
        }

        kotlinOptions {
            jvmTarget = ConfigurationData.javaVersion.toString()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = ConfigurationData.isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(platform(Libs.AndroidX.Compose.Bom))
    implementation (Libs.AndroidX.Compose.Material3)
    //TODO: remove compose ui after compose bom update.
    implementation (Libs.AndroidX.Compose.ui)
    implementation (Libs.AndroidX.Compose.uiTooling)


    implementation(Libs.AndroidX.hiltNavigationCompose)
    implementation(Libs.Hilt.hiltAndroid)
    ksp(Libs.Hilt.hiltCompiler)

    implementation(Libs.AndroidX.materialIconExtended)
    implementation (Libs.AndroidX.activityKtx)

    implementation(platform(Libs.Firebase.Bom))
    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.crashlytics)
}
