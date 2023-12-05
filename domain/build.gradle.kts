plugins {
    id("kotlin")
    kotlin("kapt")
}

java {
    sourceCompatibility = ConfigurationData.javaVersion
    targetCompatibility = ConfigurationData.javaVersion
}

dependencies {
    implementation(Libs.kotlinxCoroutinesCore)
}