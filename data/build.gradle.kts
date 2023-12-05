plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdk = ConfigurationData.compileSdk
    buildToolsVersion = ConfigurationData.buildToolsVersion
    namespace = "${ConfigurationData.applicationId}.data"

    defaultConfig {
        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk
        vectorDrawables.useSupportLibrary = ConfigurationData.useSupportLibrary
        multiDexEnabled = ConfigurationData.multiDexEnabled

        compileOptions {
            sourceCompatibility = ConfigurationData.javaVersion
            targetCompatibility = ConfigurationData.javaVersion
        }

        kotlinOptions {
            jvmTarget = ConfigurationData.javaVersion.toString()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    //domainModule
    implementation (project(":domain"))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)

    // Room and Lifecycle dependencies
    implementation (Libs.AndroidX.Room.RoomRuntime)
    implementation (Libs.AndroidX.Room.RoomKtx)
    kapt (Libs.AndroidX.Room.RoomCompiler)

    // dagger
    implementation (Libs.Dagger.dagger)
    kapt (Libs.Dagger.daggerCompiler)
}
