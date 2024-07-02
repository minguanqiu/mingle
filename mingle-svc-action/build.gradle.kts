plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
}


mavenPublishing {
    pom {
        description.set("Architecture for java web application")
    }
}