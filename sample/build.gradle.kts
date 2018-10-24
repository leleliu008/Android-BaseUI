plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(26)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(25)
        applicationId = "com.fpliu.newton.base.ui.sample"
        versionCode = 1
        versionName = "1.0.0"
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
    api(project(":library"))
    //api("com.fpliu:Android-emoji:1.0.0")

    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api("org.jetbrains.kotlin:kotlin-stdlib:1.2.50")

    api("com.android.support:support-annotations:26.1.0")
    api("com.android.support:appcompat-v7:26.1.0")
    api("com.android.support:design:26.1.0")
    api("com.fpliu:Android-RecyclerViewHelper:1.0.0")
}
