plugins {
    kotlin("multiplatform") version "2.0.20"
    id("com.android.library") version "8.5.2"
    id("maven-publish")
}
group = "com.jonathanbergen.pifk"
version = "0.0.1-SNAPSHOT.40"
val javaVersion = JavaVersion.VERSION_1_8
kotlin {
    js {
        browser()
        nodejs()
    }
    jvm()
    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = "$javaVersion"
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        dependencies {
            commonTestImplementation(kotlin("test"))
        }
    }
}
android {
    namespace = "$group"
    compileSdk = 34
    compileOptions {
        sourceCompatibility(javaVersion)
        targetCompatibility(javaVersion)
    }
}
tasks.withType<AbstractPublishToMaven> { dependsOn(tasks.check) }