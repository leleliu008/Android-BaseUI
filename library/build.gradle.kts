plugins {
    id("com.android.library")
    kotlin("android")
    
    //https://github.com/leleliu008/BintrayUploadGradlePlugin
    //https://plugins.gradle.org/plugin/com.fpliu.bintray
    id("com.fpliu.bintray").version("1.0.14")
    
    //用于构建aar和maven包
    //https://github.com/dcendents/android-maven-gradle-plugin
    id("com.github.dcendents.android-maven").version("2.0")

    //用于上传maven包到jCenter中
    //https://github.com/bintray/gradle-bintray-plugin
    id("com.jfrog.bintray").version("1.7.3")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(28)
        versionCode = 2
        versionName = "2.0.12"
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
    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api(kotlin("stdlib", rootProject.extra["kotlinVersion"] as String))

    //https://github.com/JakeWharton/RxBinding
    api("com.jakewharton.rxbinding3:rxbinding-core:3.0.0-alpha2")

    //https://dl.google.com/dl/android/maven2/index.html
    //https://developer.android.google.cn/reference/androidx/classes
    api("androidx.coordinatorlayout:coordinatorlayout:1.0.0")
    api("com.google.android.material:material:1.0.0")

    //https://github.com/uber/AutoDispose
    api("com.uber.autodispose:autodispose-ktx:1.1.0")
    api("com.uber.autodispose:autodispose-android-archcomponents:1.1.0")

    //https://bintray.com/fpliu/newton
    api("com.fpliu:kotlin-ext-android:1.0.4")
    api("com.fpliu:Android-Logger:1.0.0")
}

// 这里是groupId,必须填写,一般填你唯一的包名
group = "com.fpliu"

//这个是版本号，必须填写
version = android.defaultConfig.versionName ?: "1.0.0"

val rootProjectName: String = rootProject.name

bintrayUploadExtension {
    archivesBaseName = rootProjectName

    developerName = "leleliu008"
    developerEmail = "leleliu008@gamil.com"

    projectSiteUrl = "https://github.com/$developerName/$rootProjectName"
    projectGitUrl = "https://github.com/$developerName/$rootProjectName"

    bintrayOrganizationName = "fpliu"
    bintrayRepositoryName = "newton"
}
