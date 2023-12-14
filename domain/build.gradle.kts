plugins {
    id(Plugins.kotlin)
}

java {
    sourceCompatibility = ConfigurationData.javaVersion
    targetCompatibility = ConfigurationData.javaVersion
}

dependencies {
    implementation(Libs.kotlinxCoroutinesCore)
}