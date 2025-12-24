plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // Koin
            api(libs.koin.core)

            // Room (for AppDatabase)
            implementation(libs.room.runtime)

            // Domain
            implementation(projects.domain.core)
            implementation(projects.domain.aggregate.task)
            implementation(projects.domain.aggregate.history)
            implementation(projects.domain.aggregate.alarm)
            implementation(projects.domain.aggregate.settings)

            // Data
            implementation(projects.data.core)
            implementation(projects.data.aggregate.task)
            implementation(projects.data.aggregate.history)
            implementation(projects.data.aggregate.alarm)
            implementation(projects.data.aggregate.settings)

            // DataStore
            implementation(libs.datastore.preferences)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.segnities007.yatte.di"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
