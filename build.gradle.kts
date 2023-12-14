plugins {
    id(Plugins.ksp) version Versions.kspVersion apply false
    id(Plugins.hiltAndroid) version Versions.hiltVersion apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.Kotlin.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
