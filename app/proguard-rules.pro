# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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
# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* {*;}
-keep class com.android.volley.Request$* {*;}
-keep class com.android.volley.RequestQueue$* {*;}
-keep class com.android.volley.toolbox.HurlStack$* {*;}
-keep class com.android.volley.toolbox.ImageLoader$* {*;}
-keep class org.apache.http.** {*;}

-dontobfuscate
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.google.gson.** {*;}
-keep class com.unltm.distance.room.entity.** {*;}