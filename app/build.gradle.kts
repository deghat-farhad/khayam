plugins {
    id("com.google.devtools.ksp")
    id("com.android.application")
    kotlin("android")
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
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Libs.Dagger.dagger)
    ksp(Libs.Dagger.daggerCompiler)

    implementation(Libs.AndroidX.multidex)
    implementation(Libs.AndroidX.lifecycleViewmodelKtx)

    implementation(Libs.fragmentKtx)
    implementation(Libs.material)
    implementation(Libs.scrollingPagerIndicator)

}
