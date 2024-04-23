import org.gradle.api.JavaVersion

object ConfigurationData {
    const val applicationId = "com.vuxur.khayyam"
    const val compileSdk = 34
    const val buildToolsVersion = "34.0.0"
    const val minSdk = 21
    const val targetSdk= 34
    const val versionCode = 4
    const val versionName = "1.1.0"
    const val useSupportLibrary = true
    const val multiDexEnabled = true
    const val isMinifyEnabled = true

    val javaVersion = JavaVersion.VERSION_17
}