@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.config)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)

    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            //binaryOption("bundleId", "com.khater.rwaq")
            export("io.github.mirzemehdi:kmpnotifier:1.6.1")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.core)

            // Ktor Android
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)

            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.svg.android)

            implementation(libs.androidx.core.ktx)

            // Runtime
            implementation(libs.androidx.startup.runtime)
            implementation("com.google.android.gms:play-services-location:21.3.0")

            // Room DB
            implementation(libs.androidx.room.sqlite.wrapper)

            //referral
            implementation("com.android.installreferrer:installreferrer:2.2")

            implementation("com.paymob.sdk:Paymob-SDK:1.8.1")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // maps
//            implementation(libs.swmansion.kmpMaps.googleMaps)
//            implementation(libs.swmansion.kmpMaps.core)

            // qr code generator
            implementation("network.chaintech:qr-kit:3.1.3")

            // Geocoding
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)

            // Geolocation
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.permissions.mobile)

            api(libs.koin.core)
            api(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.navigation.compose)

            // Ktor
            implementation(libs.bundles.ktor)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.cio)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.svg)
            //    implementation(libs.coil.network.ktor)
            implementation(libs.coil.svg)
            //settings
            implementation(libs.multiplatform.settings)
            //flow settings
            implementation(libs.multiplatform.settings.coroutines)
            // Kotlinx Serialization
            implementation(libs.kotlinx.serialization.json)

            // Drop Shadow
            implementation(libs.compose.shadow)

            // Logger
            implementation(libs.kermit)

            api(libs.kmpnotifier)

            // Room DB
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.kotlinx.datetime)
            implementation("org.jetbrains.compose.ui:ui-backhandler:1.9.0")
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

            api("io.github.kevinnzou:compose-webview-multiplatform:2.0.3")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.khater.rwaq"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    ndkVersion = "26.1.10909125"
    defaultConfig {
        applicationId = "com.khater.rwaq"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            useLegacyPackaging = false
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

}
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

