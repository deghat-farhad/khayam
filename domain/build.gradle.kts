plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    //coroutines
    implementation(Libs.kotlinxCoroutinesCore)
}
