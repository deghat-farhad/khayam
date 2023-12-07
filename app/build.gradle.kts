plugins {
    id(Plugins.ksp)
    id(Plugins.application)
    id(Plugins.hiltAndroid)
    kotlin(Plugins.Kotlin.android)
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Libs.AndroidX.multidex)
    implementation(Libs.AndroidX.lifecycleViewmodelKtx)

    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.material)
    implementation(Libs.scrollingPagerIndicator)

    implementation(platform(Libs.AndroidX.Compose.Bom))
    implementation(Libs.AndroidX.Compose.Runtime)
    implementation (Libs.AndroidX.Compose.Material3)
    implementation (Libs.AndroidX.navigationCompose)

    implementation(Libs.AndroidX.hiltNavigationCompose)
    implementation(Libs.Hilt.hiltAndroid)
    ksp(Libs.Hilt.hiltCompiler)
}
