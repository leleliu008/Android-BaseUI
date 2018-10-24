import com.fpliu.gradle.bintrayUploadExtension

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        //对android-maven-gradle-plugin和gradle-bintray-plugin两个插件的包装、简化插件
        //https://github.com/leleliu008/BintrayUploadAndroidGradlePlugin
        classpath("com.fpliu:BintrayUploadGradlePlugin:1.0.0")
    }
}

apply {
    plugin("com.fpliu.bintray")
}

plugins {
    id("com.android.library")
    kotlin("android")

    //用于构建aar和maven包
    //https://github.com/dcendents/android-maven-gradle-plugin
    id("com.github.dcendents.android-maven").version("2.0")

    //用于上传maven包到jCenter中
    //https://github.com/bintray/gradle-bintray-plugin
    id("com.jfrog.bintray").version("1.7.3")
}

android {
    compileSdkVersion(27)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(25)
        versionCode = 1
        versionName = "1.0.1"
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("src/main/libs")
            aidl.srcDirs("src/main/java")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    compileOptions {
        //使用JAVA8语法解析
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    api(fileTree(mapOf(Pair("dir", "src/main/libs"), Pair("include", "*.jar"))))

    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api("org.jetbrains.kotlin:kotlin-stdlib:1.2.50")

    api("com.android.support:design:27.1.1")
    api("com.jakewharton.rxbinding2:rxbinding:2.0.0")
    api("com.trello.rxlifecycle2:rxlifecycle:2.0.1")
    // If you want to bind to Android-specific lifecycles
    api("com.trello.rxlifecycle2:rxlifecycle-android:2.0.1")
    // If you want pre-written Activities and Fragments you can subclass as     providers
    api("com.trello.rxlifecycle2:rxlifecycle-components:2.0.1")
    api("com.fpliu:Android-Logger:1.0.0")
}

// 这里是groupId,必须填写,一般填你唯一的包名
group = "com.fpliu"

//这个是版本号，必须填写
version = android.defaultConfig.versionName ?: "1.0.0"

val rootProjectName: String = rootProject.name

bintrayUploadExtension {
    developerName = "leleliu008"
    developerEmail = "leleliu008@gamil.com"

    projectSiteUrl = "https://github.com/$developerName/$rootProjectName"
    projectGitUrl = "https://github.com/$developerName/$rootProjectName"

    bintrayUserName = "fpliu"
    bintrayOrganizationName = "fpliu"
    bintrayRepositoryName = "newton"
    bintrayApiKey = ""
}

