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

# Preserve line numbers in stack traces for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep the leaderboard data model used by ApiClient JSON parsing
-keep class com.example.pildoraclicker.LeaderboardEntry { *; }

# Keep ApiClient callback interfaces (implemented as anonymous classes at call sites)
-keep interface com.example.pildoraclicker.ApiClient$* { *; }