plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleSvcRedis)
    api("org.springframework.boot:spring-boot-starter-security")
}