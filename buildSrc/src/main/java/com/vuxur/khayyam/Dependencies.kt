import Versions.hiltVersion

object Versions {
    const val kspVersion = "2.0.0-1.0.22"
    const val hiltVersion = "2.51.1"
    const val androidGradlePluginVersion = "8.5.0"
    const val kotlinVersion = "2.0.0"
    const val googleServiceVersion = "4.4.2"
    const val crashlyticsVersion = "3.0.1"
}

object Plugins {
    const val ksp = "com.google.devtools.ksp"
    const val hiltAndroid = "com.google.dagger.hilt.android"
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin = "kotlin"
    const val jetbrainsKotlinAndroid = "org.jetbrains.kotlin.android"
    const val googleService = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
    const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"
}

object Libs {
    object Coroutines {
        private const val kotlinxCoroutinesCoreVersion = "1.8.1"

        const val kotlinxCoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion"
        const val kotlinxCoroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesCoreVersion"
    }

    object AndroidX {
        private const val hiltNavigationComposeVersion = "1.2.0"
        private const val materialIconsExtendedVersion = "1.6.8"
        private const val activityKtxVersion = "1.9.0"
        private const val datastorePreferencesVersion = "1.1.1"

        const val hiltNavigationCompose =
            "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"
        const val materialIconExtended =
            "androidx.compose.material:material-icons-extended:$materialIconsExtendedVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$activityKtxVersion"
        const val datastorePreferences =
            "androidx.datastore:datastore-preferences:$datastorePreferencesVersion"

        object Room {
            private const val roomVersion = "2.6.1"
            const val RoomKtx = "androidx.room:room-ktx:$roomVersion"
            const val RoomCompiler = "androidx.room:room-compiler:$roomVersion"
        }

        object Compose {
            private const val composeBomVersion = "2024.06.00"

            const val Bom = "androidx.compose:compose-bom:$composeBomVersion"
            const val Material3 = "androidx.compose.material3:material3"
            const val ui = "androidx.compose.ui:ui"
            const val uiTooling = "androidx.compose.ui:ui-tooling"
        }
    }

    object Hilt {
        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
    }

    object Firebase {
        private const val firebaseBomVersion = "33.1.0"

        const val Bom = "com.google.firebase:firebase-bom:$firebaseBomVersion"
        const val analytics = "com.google.firebase:firebase-analytics"
        const val crashlytics = "com.google.firebase:firebase-crashlytics"
    }

    object Test {
        private const val junit5Version = "5.11.0-M2"
        private const val mockkVersion = "1.13.11"

        const val junit5 = "org.junit.jupiter:junit-jupiter-engine:$junit5Version"
        const val mockk = "io.mockk:mockk:$mockkVersion"
    }

    object Other {
        private const val capturableVersion = "2.1.0"

        const val capturable = "dev.shreyaspatil:capturable:$capturableVersion"
    }
}
