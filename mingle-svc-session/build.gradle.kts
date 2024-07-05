plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleSvcRedis)
    api("org.springframework.boot:spring-boot-starter-security")
}

mavenPublishing {
    pom {
        name.set("Mingle Service Session")
        description.set("Based on spring security, it provides the authentication and authorization functions of service.")
        url.set("https://github.com/minguanqiu/mingle-svc-session")
    }
}
