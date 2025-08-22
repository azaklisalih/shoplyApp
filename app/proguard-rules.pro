# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Hide the original source file name
-renamesourcefileattribute SourceFile

# Keep all classes in the main package
-keep class com.example.enuyguncase.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper
-keep,allowobfuscation,allowshrinking class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper

# Navigation Component
-keepnames class androidx.navigation.fragment.NavHostFragment
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

# Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Coil
-keep class coil.** { *; }
-keep interface coil.** { *; }

# Material Design
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep model classes
-keep class com.example.enuyguncase.domain.model.** { *; }
-keep class com.example.enuyguncase.data.**.dto.** { *; }
-keep class com.example.enuyguncase.data.**.entities.** { *; }

# Keep ViewModels
-keep class com.example.enuyguncase.presentation.**.viewmodel.** { *; }

# Keep UI State classes
-keep class com.example.enuyguncase.presentation.**.ui.** { *; }

# Keep Fragments and Activities
-keep class com.example.enuyguncase.presentation.**.fragment.** { *; }
-keep class com.example.enuyguncase.MainActivity { *; }
-keep class com.example.enuyguncase.SplashActivity { *; }

# Keep Application class
-keep class com.example.enuyguncase.EnuygunCaseApp { *; }

# Data Binding
-keep class androidx.databinding.** { *; }
-keep class androidx.databinding.ViewDataBinding { *; }
-keep class androidx.databinding.DataBindingUtil { *; }

# View Binding
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static *** bind(android.view.View);
}

# Keep generated binding classes
-keep class com.example.enuyguncase.databinding.** { *; }

# Keep all R classes (includes all resource types)
-keep class com.example.enuyguncase.R { *; }
-keep class com.example.enuyguncase.R$* { *; }

# Suppress warnings for missing classes
-dontwarn io.ktor.client.engine.mock.MockEngine$Companion
-dontwarn io.ktor.client.engine.mock.MockEngine
-dontwarn io.ktor.client.engine.mock.MockRequestHandleScope
-dontwarn io.ktor.client.engine.mock.MockUtilsKt

# General dontwarn rules for better compatibility
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**
-dontwarn kotlin.**
-dontwarn kotlinx.**
-dontwarn org.intellij.lang.annotations.**