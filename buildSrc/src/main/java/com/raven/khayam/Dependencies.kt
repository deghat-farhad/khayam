object Libs {
    private const val androidGradlePluginVersion = "8.2.0"
    private const val materialVersion = "1.10.0"
    private const val scrollingPagerIndicatorVersion = "1.2.5"
    private const val kotlinxCoroutinesCoreVersion = "1.7.3"
    private const val fragmentKtxVersion = "1.6.2"

    const val androidGradlePlugin = "com.android.tools.build:gradle:$androidGradlePluginVersion"
    const val material = "com.google.android.material:material:$materialVersion"
    const val scrollingPagerIndicator = "ru.tinkoff.scrollingpagerindicator:scrollingpagerindicator:$scrollingPagerIndicatorVersion"
    const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragmentKtxVersion"

    object Kotlin {
        private const val kotlinVersion = "1.9.21"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object AndroidX {
        private const val multidexVersion = "2.0.1"
        private const val lifecycleViewmodelKtxVersion = "2.6.2"

        const val multidex = "androidx.multidex:multidex:$multidexVersion"
        const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewmodelKtxVersion"

        object Room {
            private const val roomVersion = "2.6.1"
            const val RoomKtx = "androidx.room:room-ktx:$roomVersion"
            const val RoomCompiler = "androidx.room:room-compiler:$roomVersion"
        }
    }

    object Dagger {
        private const val daggerVersion = "2.49"

        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    }
}
