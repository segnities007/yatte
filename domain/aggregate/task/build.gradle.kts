plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
