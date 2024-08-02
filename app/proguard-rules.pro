# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

-dontwarn com.vuxur.khayyam.data.di.RepositoryModule
-dontwarn com.vuxur.khayyam.data.di.RepositoryModule_DatabaseFactory
-dontwarn com.vuxur.khayyam.data.di.RepositoryModule_ProvidePoemRepositoryFactory
-dontwarn com.vuxur.khayyam.data.di.RepositoryModule_ProvideSettingRepositoryFactory
-dontwarn com.vuxur.khayyam.data.di.RepositoryModule_ProvideTranslationRepositoryFactory
-dontwarn com.vuxur.khayyam.data.local.Local
-dontwarn com.vuxur.khayyam.data.local.database.PoemDatabaseDao
-dontwarn com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
-dontwarn com.vuxur.khayyam.data.mapper.PoemMapper
-dontwarn com.vuxur.khayyam.data.mapper.TranslationEntityMapper
-dontwarn com.vuxur.khayyam.data.mapper.TranslationOptionsEntityMapper
