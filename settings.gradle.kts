pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.kikugie.dev/releases/") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots/") { name = "KikuGie Snapshots" }
        maven("https://maven.teamresourceful.com/repository/maven-public/")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.10-beta.4"
    id("dev.kikugie.loom-back-compat") version "0.4.3"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val versions = listOf("26.3", "26.2", "26.1", "1.21.11")

stonecutter {
    create(rootProject) {
        versions.forEach {
            version(it).buildscript = "build.fabric.gradle.kts"
        }
        vcsVersion = versions.first()
    }
}

gradle.lifecycle.beforeProject {
    repositories {
        fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
            forRepository { maven(url) { name = alias } }
            filter { groups.forEach(::includeGroup) }
        }
        strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    }
}

rootProject.name = "Meowdding Chroma"


dependencyResolutionManagement {
    versionCatalogs {
        versions.forEach {
            val name = it.replace(".", "")
            create("libs$name") {
                from(
                    files(
                        rootProject.projectDir.resolve("gradle/${it.replace(".", "_")}-fabric.versions.toml")
                    )
                )
            }
        }
    }
}
