@file:Suppress("PropertyName")

import java.time.Instant

plugins {
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
}

val mod_version: String by properties
val mc_version: String by properties
val forge_version: String by properties
val parchment_version: String by properties
val java_lang_version: String by properties
val ccl_version: String by properties
val cbm_version: String by properties
val cct_version: String by properties

// Get mod version from CI, else suffix a timestamp (calculated here bc timestamp can change if done separately in each subproject)
val appended_version = System.getenv("AUTO_GENERATED_VERSION") ?: "$mod_version.${Instant.now().epochSecond}"
group = "mrtjp"
version = "${mc_version}-${appended_version}"

println("Starting build of ${name}, Version: $appended_version")
println("Using Forge: ${forge_version}, for Minecraft: ${mc_version}, with Mappings: $parchment_version")

// Common submodule configurations
subprojects {
    // Needs to be force-applied here to allow below configuration
    apply(plugin = "java-library")

    // Select Java version
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(java_lang_version))

    // Copy properties from root
    group = rootProject.group
    version = rootProject.version

    // Add datagen resources to source set
    sourceSets.main.get().resources {
        srcDirs("src/main/generated")
    }

    tasks {
        // Jar settings
        jar {
            archiveBaseName.set(rootProject.name)
            archiveClassifier.set(this@subprojects.name)
        }

        // Replace version tokens in mods.toml
        processResources {
            inputs.property("appended_version", appended_version)
            inputs.property("mc_version", mc_version)

            filesMatching("META-INF/mods.toml") {
                val file = hashMapOf("jarVersion" to appended_version)
                val properties = hashMapOf(
                    "file" to file,
                    "mc_version" to mc_version,
                    "forge_version" to forge_version,
                    "lang_version" to forge_version.split(".")[0],
                    "ccl_version" to ccl_version,
                    "cbm_version" to cbm_version,
                    "cct_version" to cct_version
                )

                expand(properties)
            }
        }
    }

    // Add default repositories
    repositories {
        mavenLocal()
        maven { setUrl("https://proxy-maven.covers1624.net/") }
        maven {
            setUrl("https://maven.squiddev.cc/")
            content {
                includeGroup("cc.tweaked")
            }
        }
        maven { setUrl("https://maven.blamejared.com/") }
    }
}

publishing {
    repositories {
        maven {
            setUrl("https://nexus.covers1624.net/repository/maven-releases/")
            credentials {
                username = System.getenv("MAVEN_USER")
                password = System.getenv("MAVEN_PASS")
            }
        }
    }
    publications {
        create<MavenPublication>("ProjectRed") {
            artifact(project(":api").tasks.named("jar"))
            // artifact(project(":core").tasks.named("jar"))
            // artifact(project(":expansion").tasks.named("jar"))
            // artifact(project(":exploration").tasks.named("jar"))
            // artifact(project(":fabrication").tasks.named("jar"))
            // artifact(project(":illumination").tasks.named("jar"))
            // artifact(project(":integration").tasks.named("jar"))
            // artifact(project(":transmission").tasks.named("jar"))

            pom {
                name.set(rootProject.name)
                description.set(rootProject.name)

                url.set("https://github.com/MrTJP/ProjectRed")
                scm {
                    url.set("https://github.com/MrTJP/ProjectRed")
                    connection.set("scm:git:git@github.com:MrTJP/ProjectRed.git")
                }
                issueManagement {
                    system.set("github")
                    url.set("https://github.com/MrTJP/ProjectRed/issues")
                }
                developers {
                    developer {
                        id.set("mrtjp")
                        name.set("mrtjp")
                    }
                    developer {
                        id.set("Chicken-Bones")
                        name.set("Chicken-Bones")
                    }
                    developer {
                        id.set("covers1624")
                        name.set("covers1624")
                    }
                }
            }
        }
    }
}
