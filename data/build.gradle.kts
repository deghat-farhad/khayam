plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = ConfigurationData.compileSdk
    buildToolsVersion = ConfigurationData.buildToolsVersion
    namespace = "${ConfigurationData.applicationId}.data"

    defaultConfig {
        minSdk = ConfigurationData.minSdk
        vectorDrawables.useSupportLibrary = ConfigurationData.useSupportLibrary
        multiDexEnabled = ConfigurationData.multiDexEnabled

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
        viewBinding = true
    }
}

dependencies {
    implementation (project(":domain"))

    implementation (Libs.AndroidX.Room.RoomKtx)
    ksp (Libs.AndroidX.Room.RoomCompiler)

    implementation (Libs.Dagger.dagger)
    ksp (Libs.Dagger.daggerCompiler)
}
