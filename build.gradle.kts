import me.modmuss50.mpp.ReleaseType
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogSectionUrlBuilder
import org.jetbrains.changelog.date

plugins {
    id("java-library")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.140"
    id("dev.yumi.gradle.licenser") version "2.1.1"
    id("idea")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("org.jetbrains.changelog") version "2.5.0"
}

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

version = project.extra["mod_version"]!!
group = project.extra["mod_group_id"]!!

repositories {
    maven(url = uri("https://maven.ithundxr.dev/snapshots")) // Registrate
}

base {
    archivesName.set(project.extra["mod_id"]!! as String)
}

changelog {
    version = property("mod_version")!! as String
    path = file("CHANGELOG.md").canonicalPath
    header = provider { "[${version.get()}] - ${date()}" }
    headerParserRegex = """(\d+\.\d+\.\d+(?:-[\w\d]+)?)""".toRegex()
    itemPrefix = "-"
    keepUnreleasedSection = true
    unreleasedTerm = "[Unreleased]"
    groups = listOf("Added", "Changed", "Removed", "Fixed")
    lineSeparator = "\n"
    combinePreReleases = true
    sectionUrlBuilder =
        ChangelogSectionUrlBuilder { repositoryUrl, currentVersion, previousVersion, isUnreleased -> "foo" }
    outputFile = file("release-note.txt")
}

fun getChangelog(version: String): String {
    return changelog.renderItem(
        changelog.get(version).withSummary(false),
        Changelog.OutputType.MARKDOWN
    )
}

publishMods {
    changelog = providers.provider { getChangelog(project.version.toString()) }
    type = ReleaseType.STABLE

    file.set(tasks.jar.get().archiveFile)
    modLoaders.add("neoforge")

    modrinth {
        announcementTitle.set(project.version.toString())
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
        projectId.set("super-duper-content")
        minecraftVersions.add(project.extra["minecraft_version"]!! as String)
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("thatgurkangurk/superdupercontent")
        commitish.set("main")
    }

    dryRun = true
}

license {
    // Add a license header rule, at least one must be present.
    rule(file("codeformat/HEADER"))

    // Exclude/include certain file types, defaults are provided to easily deal with Java/Kotlin projects.
    include("**/*.java") // Include Java files into the file resolution.
    include("**/*.kt") // Include Java files into the file resolution.
    exclude("**/*.properties") // Exclude properties files from the file resolution.
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = project.extra["neo_version"]!! as String

    parchment {
        mappingsVersion = project.extra["parchment_mappings_version"]!! as String
        minecraftVersion = project.extra["parchment_minecraft_version"]!! as String
    }

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!! as String)

            gameDirectory = project.file("run/client")
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!! as String)
            gameDirectory = project.file("run/server")
        }

        // This run config launches GameTestServer and runs all registered gametests, then exits.
        // By default, the server will crash when no gametests are provided.
        // The gametest system is also enabled by default for other run configs under the /test command.
        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!! as String)
        }

        create("data") {
            data()
            gameDirectory = project.file("run/data")

            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll(
                "--mod", property("mod_id") as String,
                "--all",
                "--output", file("src/generated/resources").absolutePath,
                "--existing", file("src/main/resources").absolutePath
            )
        }

        // applies to all the run configs above
        configureEach {
            // Recommended logging data for a userdev environment
            // The markers can be added/remove as needed separated by commas.
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            systemProperty("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    val modId = project.extra["mod_id"]!! as String
    mods {
        // define mod <-> source bindings
        // these are used to tell the game which sources are for which mod
        // multi mod projects should define one per mod
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}
// Include resources generated by data generators.
sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

// Sets up a dependency configuration called 'localRuntime'.
// This configuration should be used instead of 'runtimeOnly' to declare
// a dependency that will be present for runtime testing but that is
// "optional", meaning it will not be pulled by dependents of this mod.
configurations {
    val localRuntime by creating
    runtimeClasspath.get().extendsFrom(localRuntime)
}

dependencies {
    // Example optional mod dependency with JEI
    // The JEI API is declared for compile time use, while the full JEI artifact is used at runtime
    // compileOnly "mezz.jei:jei-${mc_version}-common-api:${jei_version}"
    // compileOnly "mezz.jei:jei-${mc_version}-neoforge-api:${jei_version}"
    // We add the full version to localRuntime, not runtimeOnly, so that we do not publish a dependency on it
    // localRuntime "mezz.jei:jei-${mc_version}-neoforge:${jei_version}"

    // Example mod dependency using a mod jar from ./libs with a flat dir repository
    // This maps to ./libs/coolmod-${mc_version}-${coolmod_version}.jar
    // The group id is ignored when searching -- in this case, it is "blank"
    // implementation "blank:coolmod-${mc_version}:${coolmod_version}"

    // Example mod dependency using a file as dependency
    // implementation files("libs/coolmod-${mc_version}-${coolmod_version}.jar")

    // Example project dependency using a sister or child project:
    // implementation project(":myproject")

    // For more info:
    // http://www.gradle.org/docs/current/userguide/artifact_dependencies_tutorial.html
    // http://www.gradle.org/docs/current/userguide/dependency_management.html

    implementation("com.tterrag.registrate:Registrate:${project.extra["registrate_version"]}")?.let { jarJar(it) }
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to project.extra["minecraft_version"],
        "minecraft_version_range" to project.extra["minecraft_version_range"],
        "neo_version" to project.extra["neo_version"],
        "loader_version_range" to project.extra["loader_version_range"],
        "mod_id" to project.extra["mod_id"],
        "mod_name" to project.extra["mod_name"],
        "mod_license" to project.extra["mod_license"],
        "mod_version" to project.extra["mod_version"]
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

tasks.jar {
    from("LICENSE.md")
}


// Include the output of "generateModMetadata" as an input directory for the build
// this works with both building through Gradle and the IDE.
sourceSets.main.get().resources.srcDir(generateModMetadata)

// To avoid having to run "generateModMetadata" manually, make it run on every project reload
neoForge.ideSyncTask(generateModMetadata)

// Example configuration to allow publishing using the maven-publish plugin
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = file("${project.projectDir}/repo").toURI()
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"  // Use the UTF-8 charset for Java compilation
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
