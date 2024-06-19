plugins {
    id(Plugins.ksp) version Versions.kspVersion apply false
    id(Plugins.hiltAndroid) version Versions.hiltVersion apply false
    id(Plugins.application) version Versions.androidGradlePluginVersion apply false
    id(Plugins.jetbrainsKotlinAndroid) version Versions.kotlinVersion apply false
    id(Plugins.googleService) version Versions.googleServiceVersion apply false
    id(Plugins.crashlytics) version Versions.crashlyticsVersion apply false
    id(Plugins.composeCompiler) version Versions.kotlinVersion apply false
}
