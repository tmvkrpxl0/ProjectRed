@file:Suppress("PropertyName")

plugins {
    id("java-library")
    id("net.neoforged.moddev.legacy") version "2.0.41-beta-pr-118-legacy"
}

val mc_version: String by project
val parchment_version: String by project
val forge_version: String by project

neoForge {
    version = "$mc_version-$forge_version"

    parchment {
        minecraftVersion = mc_version
        mappingsVersion = parchment_version
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}
