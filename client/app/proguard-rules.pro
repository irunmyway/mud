-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep class * implements java.io.Serializable { *; }
-keepattributes *Annotation
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
#需要忽略混淆的javabean的包名或者类
-keep class com.eztv.mud.bean.**{*;}
