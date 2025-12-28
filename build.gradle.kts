plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    // Lint & Tools
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencyCheck)
    alias(libs.plugins.aboutLibraries) apply false
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

dependencies {
    dokka(projects.composeApp)
    // Domain
    dokka(projects.domain.core)
    dokka(projects.domain.aggregate.task)
    dokka(projects.domain.aggregate.history)
    dokka(projects.domain.aggregate.alarm)
    dokka(projects.domain.aggregate.settings)
    // Data
    dokka(projects.data.core)
    dokka(projects.data.aggregate.task)
    dokka(projects.data.aggregate.history)
    dokka(projects.data.aggregate.alarm)
    dokka(projects.data.aggregate.settings)
    // Presentation
    dokka(projects.presentation.core)
    dokka(projects.presentation.navigation)
    dokka(projects.presentation.feature.home)
    dokka(projects.presentation.feature.task)
    dokka(projects.presentation.feature.history)
    dokka(projects.presentation.feature.settings)
    dokka(projects.presentation.feature.management)
    // DI
    dokka(projects.di)
}