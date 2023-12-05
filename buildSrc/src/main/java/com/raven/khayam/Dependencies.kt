package com.raven.khayam

object Versions {
    const val roomVersion = "2.5.1"
    const val rxkotlinVersion = "2.4.0"
    const val rxAndroidVersion = "2.1.1"
    const val rxJavaApiVersion = "2.2.8"
}

object Libs {
    private const val androidGradlePluginVersion = "7.2.1"
    private const val materialVersion = "1.8.0"
    private const val scrollingPagerIndicatorVersion = "1.2.1"
    private const val designVersion = "28.0.0"

    const val androidGradlePlugin = "com.android.tools.build:gradle:$androidGradlePluginVersion"
    const val material = "com.google.android.material:material:$materialVersion"
    const val scrollingPagerIndicator = "ru.tinkoff.scrollingpagerindicator:scrollingpagerindicator:$scrollingPagerIndicatorVersion"
    const val design = "com.android.support:design:$designVersion"

    object Kotlin {
        private const val kotlinVersion = "1.8.21"

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion"
    }

    object AndroidX {
        private const val appcompatVersion = "1.4.1"
        private const val viewpagerVersion = "1.0.0"
        private const val coreKtxVersion = "1.10.0"
        private const val constraintlayoutVersion = "2.1.4"
        private const val multidexVersion = "2.0.1"
        private const val lifecycleViewmodelKtxVersion = "2.6.1"

        const val appcompat = "androidx.appcompat:appcompat:$appcompatVersion"
        const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:$constraintlayoutVersion"
        const val viewpager2 = "androidx.viewpager2:viewpager2:$viewpagerVersion"
        const val multidex = "androidx.multidex:multidex:$multidexVersion"
        const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewmodelKtxVersion"
    }

    object Dagger {
        private const val daggerVersion = "2.44"

        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    }

    object LifeCycle {
        private const val lifecycleVersion = "1.1.1"

        const val runtime = "android.arch.lifecycle:runtime:$lifecycleVersion"
        const val extensions =  "android.arch.lifecycle:extensions:$lifecycleVersion"
        const val compiler = "android.arch.lifecycle:compiler:$lifecycleVersion"
    }
}
