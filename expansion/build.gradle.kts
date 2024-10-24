import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
    id("net.neoforged.moddev.legacy") version "2.0.41-beta-pr-118-legacy"
    id("net.darkhax.curseforgegradle")
}

val mod_id = "projectred_expansion"
val mc_version: String by project
val parchment_version: String by project
val forge_version: String by project
val java_lang_version: String by project

val ccl_version: String by project
val cbm_version: String by project

neoForge {
    version = "$mc_version-$forge_version"

    parchment {
        minecraftVersion = mc_version
        mappingsVersion = parchment_version
    }

    // accessTransformer = file("../core/src/main/resources/META-INF/accesstransformer.cfg")

    runs {
        create("data") {
            data()
            programArguments.addAll(
                "--mod",
                mod_id,
                "--all",
                "--output",
                file("src/main/generated").absolutePath,
                "--existing",
                file("src/main/resources").absolutePath
            )
        }

        mods {
            create(mod_id) {
                sourceSet(sourceSets.main.get())
            }
        }
    }
}

dependencies {
    modImplementation("io.codechicken:CodeChickenLib:${mc_version}-${ccl_version}:universal")
    modImplementation("io.codechicken:CBMultipart:${mc_version}-${cbm_version}:universal")

    modImplementation(project(":core"))
    additionalRuntimeClasspath(project(":core"))
}

mixin {
    // This will automatically add manifest entry to jar
    add(sourceSets.main.get(), "mixins.projectred.expansion.refmap.json")
    config("mixins.projectred.expansion.json")

    /*debug {
        verbose = true
        export = true
    }*/
}


tasks {
    create<TaskPublishCurseForge>("publishToCurseForge") {
        apiToken = System.getenv("CURSE_TOKEN") ?: "XXX"
        val projectId = "229048"
        val toUpload = upload(projectId, jar)

        // Core
        toUpload.releaseType = System.getenv("CURSE_RELEASE_TYPE") ?: "alpha"
        toUpload.changelogType = "markdown"
        toUpload.changelog = rootProject.file("CHANGELOG.md")
        toUpload.addRequirement("project-red-core", )

        // Java/ForgeGradle integrations don"t work after 1.18.2 port
        toUpload.addGameVersion(mc_version)
        toUpload.addGameVersion("Java $java_lang_version")
    }
}
