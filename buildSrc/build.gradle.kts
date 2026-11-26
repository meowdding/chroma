plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version libs.versions.kotlin.asProvider().get()
}

repositories {
    gradlePluginPortal()
    maven("https://maven.teamresourceful.com/repository/maven-public/")
    maven("https://maven.kikugie.dev/snapshots")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}

fun plugin(provider: Provider<PluginDependency>): Provider<String> = provider.map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

kotlin.jvmToolchain(25)

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin.api)
    //implementation(plugin(libs.plugins.meowdding.auto.mixins))
    implementation("dev.kikugie.stonecutter:dev.kikugie.stonecutter.gradle.plugin:0.10")
}