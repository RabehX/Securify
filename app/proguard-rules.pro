# ================================================================================================
# General Attributes
# ================================================================================================
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault, *Annotation*
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# ================================================================================================
# Window Extensions & Compose Tooling
# ================================================================================================
-dontwarn androidx.compose.animation.tooling.**
-dontwarn androidx.window.extensions.**
-dontwarn androidx.window.sidecar.**

# ================================================================================================
# REI Library - Native Methods
# ================================================================================================
-keep class io.github.rabehx.rei.** { *; }
-keepclasseswithmembernames class io.github.rabehx.rei.** {
    native <methods>;
}

# ================================================================================================
# Retrofit & OkHttp
# ================================================================================================
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions**

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ================================================================================================
# Gson
# ================================================================================================
-dontwarn sun.misc.**

-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# ================================================================================================
# Kotlinx Serialization
# ================================================================================================
-dontnote kotlinx.serialization.AnnotationsKt

-keep,includedescriptorclasses class io.github.rabehx.securify.**$$serializer { *; }
-keepclassmembers class io.github.rabehx.securify.** {
    *** Companion;
}
-keepclasseswithmembers class io.github.rabehx.securify.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class io.github.rabehx.securify.** { *; }

# ================================================================================================
# Application Model Classes
# ================================================================================================
-keep class io.github.rabehx.securify.network.model.** { *; }
-keep class io.github.rabehx.securify.datastore.model.** { *; }

# ================================================================================================
# Jetpack Compose & App Classes
# ================================================================================================
-keep class io.github.rabehx.securify.viewmodel.** { *; }
-keep class io.github.rabehx.securify.repository.** { *; }
-keep interface io.github.rabehx.securify.network.api.** { *; }
-keep interface io.github.rabehx.securify.Route { *; }
-keep class io.github.rabehx.securify.Route$* { *; }
-keep class io.github.rabehx.securify.utils.NetworkResult { *; }
-keep class io.github.rabehx.securify.utils.NetworkResult$* { *; }
