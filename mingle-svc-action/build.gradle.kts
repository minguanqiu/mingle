plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
}


mavenPublishing {
    pom {
        description.set("Provides the functions of logic-related modules that make up service")
    }
}