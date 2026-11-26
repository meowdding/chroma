import dev.kikugie.stonecutter.controller.task.StonecutterTaskMutexService

plugins {
    id("dev.kikugie.loom-back-compat")
    id("versioned-catalogues")
    `maven-publish`
    kotlin("jvm")
}

version = "${project.version}+${sc.current.version}"
base.archivesName = providers.gradleProperty("id").get()

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_1_8
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

dependencies {
    minecraft(versionedCatalog["minecraft"])
    loomx.applyMojangMappings()

    modImplementation(versionedCatalog["fabric-loader"])
    modImplementation(versionedCatalog["fabric-language-kotlin"])

    modImplementation(versionedCatalog["fabric-api"])
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
            "minecraft" to versionedCatalog.version("minecraft"),
            "fl" to versionedCatalog.version("fabric-loader"),
            "flk" to versionedCatalog.version("fabric-language-kotlin"),
        )
        inputs.properties(props)

        filesMatching("fabric.mod.json") { expand(props) }
    }

    withType<AbstractPublishToMaven>().configureEach {
        usesService(StonecutterTaskMutexService.create(gradle, name))
    }
}

//publishing {
//    repositories {
//        val mavenUsername: String? = env.fetchOrNull("MAVEN_USERNAME")
//        val mavenPassword: String? = env.fetchOrNull("MAVEN_PASSWORD")
//        if (sc.properties.getAs<Boolean>("publish.maven") && mavenUsername != null && mavenPassword != null) maven(sc.properties["publish.maven_url"]) {
//            name = "Maven"
//            credentials {
//                username = mavenUsername
//                password = mavenPassword
//            }
//        }
//    }
//
//    publications {
//        register<MavenPublication>("maven") {
//            groupId = "me.owdding"
//            artifactId = "${sc.properties["mod.id"]}-fabric"
//            from(components["java"])
//        }
//    }
//}
