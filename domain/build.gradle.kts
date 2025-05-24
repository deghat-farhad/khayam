plugins {
    id(Plugins.kotlin)
}

java {
    sourceCompatibility = ConfigurationData.javaVersion
    targetCompatibility = ConfigurationData.javaVersion
}

kotlin {
    jvmToolchain(ConfigurationData.javaVersionInt)
}

dependencies {
    implementation(Libs.Coroutines.kotlinxCoroutinesCore)
}