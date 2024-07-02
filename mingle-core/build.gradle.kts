plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api(libs.guava)
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:${libs.versions.springdoc.get()}")
    api("org.springdoc:springdoc-openapi-starter-webmvc-api:${libs.versions.springdoc.get()}")
    api("org.apache.commons:commons-text:1.11.0")
}

mavenPublishing {
    pom {
        description.set("Architecture for java web application")
    }
}








