import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.kikugie.loom-back-compat")
    kotlin("jvm")
    alias(libs.plugins.kotlin.symbol.processor)
    alias(libs.plugins.meowdding.auto.mixins)
    id("versioned-catalogues")
}

version = "${project.version}+${sc.current.version}"
base.archivesName = providers.gradleProperty("id").get()

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")
    accessWidenerPath = sc.process(
        rootProject.file("src/main/resources/meowdding_chroma.ct"),
        "build/processed.ct"
    )

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs["client"].apply {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
    }
}

repositories {
    fun scopedMaven(url: String, vararg paths: String) = exclusiveContent { forRepository { maven(url) }; filter { paths.forEach(::includeGroupAndSubgroups) } }
    mavenCentral()
    scopedMaven(
        "https://maven.teamresourceful.com/repository/maven-public/",
        "earth.terrarium",
        "com.teamresourceful",
        "tech.thatgravyboat",
        "me.owdding",
        "com.terraformersmc"
    )
}

dependencies {
    minecraft(versionedCatalog["minecraft"])
    loomx.applyMojangMappings()

    modImplementation(versionedCatalog["fabric-loader"])
    modImplementation(versionedCatalog["fabric-language-kotlin"])

    modImplementation(versionedCatalog["fabric-api"])

    compileOnly(versionedCatalog["meowdding.ktmodules"])
    compileOnly(versionedCatalog["meowdding.ktcodecs"])

    ksp(versionedCatalog["meowdding.ktmodules"])
    ksp(versionedCatalog["meowdding.ktcodecs"])
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    withType<ProcessResources>().configureEach {
        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    processResources {
        val props = mapOf(
            "id" to providers.gradleProperty("id").get(),
            "name" to providers.gradleProperty("name").get(),
            "version" to project.version,
            "minecraft" to versionedCatalog.version("minecraft-range"),
            "fl" to versionedCatalog.version("fabric-loader"),
            "flk" to versionedCatalog.version("fabric-language-kotlin"),
        )
        inputs.properties(props)

        filesMatching("fabric.mod.json") { expand(props) }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        optIn.addAll(
            "kotlin.time.ExperimentalTime",
            "kotlin.ExperimentalVersionOverloading",
            "kotlin.contracts.ExperimentalContracts",
        )
        freeCompilerArgs.addAll(
            "-Xbackend-threads=1",
            "-Xcontext-sensitive-resolution",
            "-Xnullability-annotations=@org.jspecify.annotations:ignore",
            "-Xwarning-level=DEPRECATION:disabled",
            "-Xwarning-level=REDUNDANT_VISIBILITY_MODIFIER:disabled",
        )
        allWarningsAsErrors = true
        extraWarnings = true
    }
}

ksp {
    arg("meowdding.project_name", "MeowddingLib")
    arg("meowdding.package", "me.owdding.lib.generated")
}

autoMixins {
    mixinPackage = "me.owdding.chroma.mixins"
    projectName = "meowdding_chroma"
}
