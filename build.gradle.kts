import org.gradle.plugins.ide.eclipse.model.Classpath
import org.gradle.plugins.ide.eclipse.model.Container
import org.gradle.plugins.ide.eclipse.model.Library
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    application
    checkstyle
    eclipse
    id("com.github.spotbugs") version "6.5.10"
    jacoco
    pmd
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

checkstyle {
    toolVersion = "10.18.0"
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    isShowViolations = true
}

pmd {
    toolVersion = "7.26.0"
    isConsoleOutput = true
    isIgnoreFailures = false
    ruleSets = emptyList()
    ruleSetConfig = resources.text.fromFile(file("${rootDir}/config/pmd/pmd.xml"))
}

spotbugs {
    toolVersion = "4.10.3"
    ignoreFailures.set(false)
    excludeFilter.set(file("${rootDir}/config/spotbugs/exclude.xml"))
}

jacoco {
    toolVersion = "0.8.15"
}

tasks.named<Test>("test") {
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
    }
}

tasks.named("check") {
    dependsOn(
        tasks.named("checkstyleMain"),
        tasks.named("pmdMain"),
        tasks.named("spotbugsMain"),
        tasks.named("jacocoTestReport")
    )
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
