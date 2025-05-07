import java.text.SimpleDateFormat
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

fun getVersionCodeAndName(): Triple<Int, String, String> {
    val versionPropsFile = File("version.properties")

    if (!versionPropsFile.exists()) {
        versionPropsFile.writeText("VERSION_CODE=1\n")
    }

    val versionProps = Properties().apply {
        load(versionPropsFile.inputStream())
    }

    val currentCode = try {
        versionProps.getProperty("VERSION_CODE")?.toInt() ?: 1
    } catch (e: Exception) {
        1
    }

    val newCode = currentCode + 1
    versionProps.setProperty("VERSION_CODE", newCode.toString())
    versionProps.store(versionPropsFile.writer(), null)

    var major = 1
    var minor = 2
    var patch = newCode

    if (patch > 100) {
        minor += patch / 100
        patch %= 100
    }

    if (minor > 100) {
        major += minor / 100
        minor %= 100
    }

    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
    val versionName = "$major.$minor.$patch"

    return Triple(newCode, versionName, timestamp)
}

val (code, name, date) = getVersionCodeAndName()

android {
    namespace = "com.vibedev.imusicplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vibedev.imusicplayer"
        minSdk = 21
        targetSdk = 35
        versionCode = code
        versionName = name

        buildConfigField("String", "BUILD_DATE", "\"$date\"")
        buildConfigField("String", "VERSION_NAME", "\"$name\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.dexter)
}