plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.springBootDependencies))
    constraints {
        project.rootProject.subprojects.forEach { subproject ->
            if (subproject != project) {
                api(subproject)
            }
        }
    }
}

mavenPublishing {
    pom {
        name.set("Mingle BOM")
        description.set("Mingle BOM")
        url.set("https://github.com/minguanqiu/mingle-bom")
    }
}


