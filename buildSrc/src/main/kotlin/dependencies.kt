@file:Suppress("unused")

object Ext {
    val versionCode = 1
    val versionName = "1.0.0"
    val groupName = "com.github.lekaha"
    val artifact = "carddecklayoutmanager"
}

object Android {
    val compileSdk = 28
    val minSdk = 20
    val targetSdk = compileSdk
    val applicationId = "${Ext.groupName}.${Ext.artifact}"
    val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Versions {
    val kotlinVersion = "1.3.11"
    val androidPluginVersion = "3.3.0"

    val appCompatVersion = "1.1.0-alpha01"
    val recyclerviewVersion = "1.1.0-alpha01"
    val constraintlayoutVersion = "2.0.0-alpha3"
    val ktxVersion = "1.0.1"

    val junitVersion = "4.12"
    val runnerVersion = "1.1.1"
    val espressoVersion = "3.1.1"
}

object Plugins {
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    val android = "com.android.tools.build:gradle:${Versions.androidPluginVersion}"
}

object Dependencies {
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerviewVersion}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayoutVersion}"
    val ktx = "androidx.core:core-ktx:${Versions.ktxVersion}"

    val junit = "junit:junit:${Versions.junitVersion}"
    val runner = "androidx.test:runner:${Versions.runnerVersion}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
}