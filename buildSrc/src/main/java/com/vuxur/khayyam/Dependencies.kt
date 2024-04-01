import Versions.hiltVersion

object Versions {
    const val kspVersion = "1.9.21-1.0.15"
    const val hiltVersion = "2.49"
    const val androidGradlePluginVersion = "8.2.0"
    const val kotlinVersion = "1.9.21"
}

object Plugins {
    const val ksp = "com.google.devtools.ksp"
    const val hiltAndroid = "com.google.dagger.hilt.android"
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin = "kotlin"
    const val jetbrainsKotlinAndroid = "org.jetbrains.kotlin.android"q
}

object Libs {
    private const val kotlinxCoroutinesCoreVersion = "1.7.3"

    const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion"

    object AndroidX {
        private const val hiltNavigationComposeVersion = "1.1.0"
        private const val materialIconsExtendedVersion = "1.5.4"
        private const val activityKtxVersion = "1.8.2"

        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"
        const val materialIconExtended = "androidx.compose.material:material-icons-extended:$materialIconsExtendedVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$activityKtxVersion"

        object Room {
            private const val roomVersion = "2.6.1"
            const val RoomKtx = "androidx.room:room-ktx:$roomVersion"
            const val RoomCompiler = "androidx.room:room-compiler:$roomVersion"
        }

        object Compose {
            private const val composeBomVersion = "2023.10.01"
            private const val composeUiVersion = "1.6.0-beta03"

            const val Bom = "androidx.compose:compose-bom:$composeBomVersion"
            const val Material3 = "androidx.compose.material3:material3"
            const val ui = "androidx.compose.ui:ui:$composeUiVersion"
            const val uiTooling = "androidx.compose.ui:ui-tooling"
        }
    }

    object Dagger {
        private const val daggerVersion = "2.49"

        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    }

    object Hilt {
        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
    }
}
