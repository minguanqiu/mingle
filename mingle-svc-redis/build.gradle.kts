plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-aop")
}

mavenPublishing {
    pom {
        name.set("Mingle Service Redis")
        description.set("Based on spring data redis, it provides redis architecture.")
        url.set("https://github.com/minguanqiu/mingle-svc-redis")
    }
}
