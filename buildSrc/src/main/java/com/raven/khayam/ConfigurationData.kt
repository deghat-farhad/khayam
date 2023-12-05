import org.gradle.api.JavaVersion

object ConfigurationData {
    const val applicationId = "com.raven.khayam"
    const val compileSdk = 34
    const val buildToolsVersion = "34.0.0"
    const val minSdk = 21
    const val targetSdk= 34
    const val versionCode = 1
    const val versionName = "1.0"
    const val useSupportLibrary = true
    const val multiDexEnabled = true
    const val isMinifyEnabled = false

    val javaVersion = JavaVersion.VERSION_17
}