rootProject.name = "yatte"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
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
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// App
include(":composeApp")

// Domain Layer
include(":domain:core")
include(":domain:aggregate:task")
include(":domain:aggregate:history")
include(":domain:aggregate:alarm")
include(":domain:aggregate:settings")
include(":domain:aggregate:category")

// Data Layer
include(":data:core")
include(":data:aggregate:task")
include(":data:aggregate:history")
include(":data:aggregate:alarm")
include(":data:aggregate:settings")
include(":data:aggregate:category")

// Presentation Layer
include(":presentation:designsystem")
include(":presentation:core")
include(":presentation:navigation")
include(":presentation:feature:home")
include(":presentation:feature:task")
include(":presentation:feature:history")
include(":presentation:feature:settings")
include(":presentation:feature:management")
include(":presentation:feature:category")

// DI
include(":di")