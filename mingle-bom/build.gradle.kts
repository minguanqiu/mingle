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
            if(subproject != project) {
                api(subproject)
            }
        }
    }
}
publishing {
    publications.create<MavenPublication>("maven") {
        from(project.components["javaPlatform"])
    }
}


