plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)
        applicationId = "com.fpliu.newton.ui.base.sample"
        versionCode = 1
        versionName = "1.0.0"
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("src/main/libs")
            java.srcDirs("src/main/kotlin")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(project(":library"))
    //api("com.fpliu:Android-BaseUI:2.0.11")
    
    api("com.fpliu:Android-CustomDimen:1.0.0")
    api("com.fpliu:Android-StatusBar-Util:1.0.1")
    api("com.fpliu:Android-Font-Assets-Alibaba_PuHuiTi_Light:1.0.0")

    //https://github.com/airbnb/lottie-android
    api("com.airbnb.android:lottie:3.0.7")

    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api(kotlin("stdlib", rootProject.extra["kotlinVersion"] as String))
}
