plugins {
    id("com.google.devtools.ksp") version Versions.kspVersion apply false
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
