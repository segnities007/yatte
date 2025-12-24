plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // 純粋Kotlinのみ、フレームワーク依存なし
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
