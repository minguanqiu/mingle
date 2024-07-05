plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
}


mavenPublishing {
    pom {
        name.set("Mingle Service Action")
        description.set("Provides the functions of logic-related modules that make up service")
        url.set("https://github.com/minguanqiu/mingle-svc-action")
    }
}
