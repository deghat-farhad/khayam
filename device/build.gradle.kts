plugins {
    id(Plugins.ksp)
    id(Plugins.library)
    id(Plugins.jetbrainsKotlinAndroid)
    id(Plugins.hiltAndroid)
}

android {
    compileSdk = ConfigurationData.compileSdk
    buildToolsVersion = ConfigurationData.buildToolsVersion
    namespace = "${ConfigurationData.applicationId}.device"

    defaultConfig {
        minSdk = ConfigurationData.minSdk
        vectorDrawables.useSupportLibrary = ConfigurationData.useSupportLibrary
        multiDexEnabled = ConfigurationData.multiDexEnabled

        compileOptions {
            sourceCompatibility = ConfigurationData.javaVersion
            targetCompatibility = ConfigurationData.javaVersion
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

kotlin {
    jvmToolchain(ConfigurationData.javaVersionInt)
}

dependencies {
    implementation(project(":domain"))

    implementation(Libs.AndroidX.Room.RoomKtx)
    ksp(Libs.AndroidX.Room.RoomCompiler)

    implementation(Libs.AndroidX.hiltNavigationCompose)
    implementation(Libs.Hilt.hiltAndroid)
    ksp(Libs.Hilt.hiltCompiler)

    implementation(Libs.AndroidX.datastorePreferences)
    implementation(Libs.AndroidX.materialIconExtended)
}