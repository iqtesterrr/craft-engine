import org.gradle.process.internal.ExecException
import java.io.ByteArrayOutputStream

plugins {
    id("java")
}

val git : String = versionBanner()
val builder : String = builder()
ext["git_version"] = git
ext["builder"] = builder

subprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        maven("https://jitpack.io/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    tasks.processResources {
        filteringCharset = "UTF-8"

        filesMatching(arrayListOf("craft-engine.properties")) {
            expand(rootProject.properties)
        }

        filesMatching(arrayListOf("commands.yml", "config.yml", "*/*.yml", "ignite.mod.json")) {
            expand(
                Pair("project_version", rootProject.properties["project_version"]),
                Pair("config_version", rootProject.properties["config_version"])
            )
        }
    }
}

fun versionBanner(): String {
    val os = ByteArrayOutputStream()
    try {
        project.exec {
            commandLine = "git rev-parse --short=8 HEAD".split(" ")
            standardOutput = os
        }
    } catch (e: ExecException) {
        return "Unknown"
    }
    return String(os.toByteArray()).trim()
}

fun builder(): String {
    val os = ByteArrayOutputStream()
    try {
        project.exec {
            commandLine = "git config user.name".split(" ")
            standardOutput = os
        }
    } catch (e: ExecException) {
        return "Unknown"
    }
    return String(os.toByteArray()).trim()
}