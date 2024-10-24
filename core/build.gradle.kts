import groovy.lang.Closure
import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
    id("net.neoforged.moddev.legacy") version "2.0.41-beta-pr-118-legacy"
    id("net.darkhax.curseforgegradle")
}

val mod_id = "projectred_core"
val mc_version: String by project
val parchment_version: String by project
val forge_version: String by project
val java_lang_version: String by project

val ccl_version: String by project
val cbm_version: String by project
val cct_version: String by project
val jei_version: String by project

neoForge {
    version = "$mc_version-$forge_version"

    parchment {
        minecraftVersion = mc_version
        mappingsVersion = parchment_version
    }

    setAccessTransformers(file("src/main/resources/META-INF/accesstransformer.cfg"))
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

    // JEI
    modCompileOnly("mezz.jei:jei-${mc_version}-common-api:${jei_version}")
    modCompileOnly("mezz.jei:jei-${mc_version}-forge-api:${jei_version}")
    modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")

    // CCTweaked
    modCompileOnly("cc.tweaked:cc-tweaked-${mc_version}-core-api:${cct_version}")
    modCompileOnly("cc.tweaked:cc-tweaked-${mc_version}-forge-api:${cct_version}")
    modRuntimeOnly("cc.tweaked:cc-tweaked-${mc_version}-forge:${cct_version}")

    api(project(":api"))
    additionalRuntimeClasspath(project(":api"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    create<TaskPublishCurseForge>("publishToCurseForge") {
        apiToken = System.getenv("CURSE_TOKEN") ?: "XXX"
        val projectId = "228702"
        val toUpload = upload(projectId, jar)

        // Core
        toUpload.releaseType = System.getenv("CURSE_RELEASE_TYPE") ?: "alpha"
        toUpload.changelogType = "markdown"
        toUpload.changelog = rootProject.file("CHANGELOG.md")
        toUpload.addRequirement("codechicken-lib-1-8", )
        toUpload.addRequirement("cb-multipart")

        // Java/ForgeGradle integrations don"t work after 1.18.2 port
        toUpload.addGameVersion(mc_version)
        toUpload.addGameVersion("Java $java_lang_version")
    }
}
