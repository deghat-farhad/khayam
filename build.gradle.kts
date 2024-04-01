plugins {
    id(Plugins.ksp) version Versions.kspVersion apply false
    id(Plugins.hiltAndroid) version Versions.hiltVersion apply false
    id(Plugins.application) version Versions.androidGradlePluginVersion apply false
    id(Plugins.jetbrainsKotlinAndroid) version Versions.kotlinVersion apply false
}
