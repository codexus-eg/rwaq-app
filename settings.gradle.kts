rootProject.name = "Rwaq"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.google.com/")
            name = "Google"
        }
        maven {
            url = rootProject.projectDir.toURI().resolve("libs")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        google()
        mavenCentral()
        maven {
            url = rootProject.projectDir.toURI().resolve("libs")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

include(":composeApp")