import org.gradle.plugins.ide.eclipse.model.Classpath
import org.gradle.plugins.ide.eclipse.model.Container
import org.gradle.plugins.ide.eclipse.model.Library

plugins {
    application
    eclipse
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("tst"))
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.0")
}

application {
    mainClass.set("games.match3.GardenMatchApp")
}

tasks.test {
    useJUnitPlatform()
}

eclipse {
    project {
        natures.remove("org.eclipse.buildship.core.gradleprojectnature")
        buildCommands.removeIf {
            it.name == "org.eclipse.buildship.core.gradleprojectbuilder"
        }
    }
    classpath {
        defaultOutputDir = file("bin")
        file {
            whenMerged {
                val classpath = this as Classpath
                classpath.entries.removeIf {
                    it is Library || it is Container && it.path.contains("buildship")
                }
                if (classpath.entries.none {
                        it is Container && it.path == "org.eclipse.jdt.junit.JUNIT_CONTAINER/6"
                    }) {
                    classpath.entries.add(Container("org.eclipse.jdt.junit.JUNIT_CONTAINER/6"))
                }
            }
        }
    }
}

tasks.named("eclipseJdt") {
    doLast {
        val prefs = layout.projectDirectory.file(".settings/org.eclipse.jdt.core.prefs").asFile
        if (prefs.isFile) {
            val cleaned = prefs.readLines()
                .dropWhile { it == "#" || it.matches(Regex("#[A-Z][A-Za-z]{2} .* \\d{4}")) }
            prefs.writeText(cleaned.joinToString(System.lineSeparator(), postfix = System.lineSeparator()))
        }
    }
}

tasks.register<JavaExec>("runGardenMatch") {
    group = "application"
    description = "Runs the GardenMatch Swing app."
    mainClass.set("games.match3.GardenMatchApp")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runMergeMansion") {
    group = "application"
    description = "Runs the MergeMansion Swing app."
    mainClass.set("games.merge.MergeMansionApp")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runWordSprout") {
    group = "application"
    description = "Runs the WordSprout Swing app."
    mainClass.set("games.wordconnect.WordSproutApp")
    classpath = sourceSets["main"].runtimeClasspath
}
