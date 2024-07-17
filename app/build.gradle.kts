plugins {
    id(Plugins.ksp)
    id(Plugins.application)
    id(Plugins.hiltAndroid)
    id(Plugins.jetbrainsKotlinAndroid)
    id(Plugins.googleService)
    id(Plugins.crashlytics)
    id(Plugins.composeCompiler)
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

        buildFeatures {
            buildConfig = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = ConfigurationData.isMinifyEnabled
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
    lint {
        baseline = File("lint-baseline.xml")
        warningsAsErrors = true
        textReport = true
        textOutput = File("stdout")
        explainIssues = false
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Libs.Coroutines.kotlinxCoroutinesCore)

    implementation(platform(Libs.AndroidX.Compose.Bom))
    implementation(Libs.AndroidX.Compose.Material3)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.coreSplashscreen)

    implementation(Libs.AndroidX.hiltNavigationCompose)
    implementation(Libs.Hilt.hiltAndroid)
    ksp(Libs.Hilt.hiltCompiler)

    implementation(Libs.AndroidX.materialIconExtended)
    implementation(Libs.AndroidX.activityKtx)

    implementation(platform(Libs.Firebase.Bom))
    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.crashlytics)

    testImplementation(Libs.Test.junit5)
    testImplementation(Libs.Test.mockk)
    testImplementation(Libs.Coroutines.kotlinxCoroutinesTest)
}
