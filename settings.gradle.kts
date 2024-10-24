pluginManagement {
    repositories {
        maven {
            name = "NeoForge"
            setUrl("https://maven.neoforged.net/releases")
            content {
                includeGroup("net.minecraftforge")
                includeGroup("net.neoforged")
                includeGroup("net.neoforged.gradle")
                includeModule("codechicken", "DiffPatch")
                includeModule("net.covers1624", "Quack")
            }
        }
        maven {
            name = "Maven for PR #118" // https://github.com/neoforged/ModDevGradle/pull/118
            setUrl("https://prmaven.neoforged.net/ModDevGradle/pr118")
            content {
                includeModule("net.neoforged.moddev.legacy", "net.neoforged.moddev.legacy.gradle.plugin")
                includeModule("net.neoforged.moddev", "net.neoforged.moddev.gradle.plugin")
                includeModule("net.neoforged", "moddev-gradle")
                includeModule("net.neoforged.moddev.repositories", "net.neoforged.moddev.repositories.gradle.plugin")
            }
        }
        gradlePluginPortal()
    }

    plugins {
        id("net.neoforged.moddev.legacy") version "2.0.41-beta-pr-118-legacy"
        id("net.covers1624.signing") version "1.1.3" apply false
        id("net.darkhax.curseforgegradle") version "1.1.25" apply false
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("api")
include("core", "expansion")

rootProject.name = "ProjectRed"