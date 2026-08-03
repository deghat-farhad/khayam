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

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(Libs.Coroutines.kotlinxCoroutinesCore)

    testImplementation(Libs.Test.junit5)
    testRuntimeOnly(Libs.Test.junitPlatformLauncher)
}
