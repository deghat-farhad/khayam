import com.raven.khayam.ConfigurationData
import com.raven.khayam.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdk = ConfigurationData.compileSdk
    buildToolsVersion = ConfigurationData.buildToolsVersion
    namespace = ConfigurationData.applicationId

    defaultConfig {
        applicationId = ConfigurationData.applicationId
        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk
        versionCode = ConfigurationData.versionCode
        versionName = ConfigurationData.versionName
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
    implementation( project(":data"))
    implementation( project(":domain"))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.constraintlayout)

    // dagger
    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.daggerCompiler)

    //lifecycle
    implementation(Libs.LifeCycle.runtime)
    implementation(Libs.LifeCycle.extensions)
    annotationProcessor(Libs.LifeCycle.compiler)

    //Material
    implementation(Libs.material)

    //ViewPagerAndIndicator
    implementation(Libs.AndroidX.viewpager2)
    implementation(Libs.scrollingPagerIndicator)

    //Design
    implementation(Libs.design)

    implementation(Libs.AndroidX.multidex)

    implementation(Libs.AndroidX.lifecycleViewmodelKtx)
}
