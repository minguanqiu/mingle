import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java")
    id("jvm-test-suite")
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.lombok) apply false
    alias(libs.plugins.mavenPublish)
}

allprojects {
    group = "io.github.minguanqiu"
    version = "2.0.2"
}

subprojects {
    plugins.apply(rootProject.libs.plugins.mavenPublish.get().pluginId)
    repositories {
        mavenCentral()
    }
    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        coordinates(group.toString(), project.name, version.toString())
        signAllPublications()
        pom {
            name.set("Mingle")
            description.set("Infrastructure for java web application")
            url.set("https://github.com/minguanqiu/mingle")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    name.set("Qiu Guan Ming")
                    url.set("https://github.com/minguanqiu")
                }
            }
            scm {
                url.set("https://github.com/minguanqiu/mingle/")
                connection.set("scm:git:git://github.com/minguanqiu/mingle.git")
                developerConnection.set("scm:git:ssh://git@github.com/minguanqiu/mingle.git")
            }
        }

    }
}

subprojects {
    val project = this@subprojects
    apply(plugin = rootProject.libs.plugins.mavenPublish.get().pluginId)
    if (project.name == "mingle-bom") return@subprojects
    apply(plugin = "java")
    apply(plugin = "jvm-test-suite")
    apply(plugin = rootProject.libs.plugins.springDependencyManagement.get().pluginId)
    apply(plugin = rootProject.libs.plugins.lombok.get().pluginId)
    apply(plugin = "checkstyle")

    val checkstyleConfig: Configuration by configurations.creating
    dependencies {
        checkstyleConfig(rootProject.libs.checkStyle) {
            isTransitive = false
        }
    }

    configure<CheckstyleExtension> {
        config = resources.text.fromArchiveEntry(checkstyleConfig, "google_checks.xml")
        toolVersion = rootProject.libs.versions.checkStyle.get()
        sourceSets = listOf(project.sourceSets["main"])
    }

    testing {
        suites {
            val test by getting(JvmTestSuite::class) {
                useJUnitJupiter()
            }
            dependencies {
                testImplementation(rootProject.libs.springBootTest)
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.release.set(17)
        options.encoding = "UTF-8"
    }

}

