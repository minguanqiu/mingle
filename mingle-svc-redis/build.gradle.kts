plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-aop")
}
