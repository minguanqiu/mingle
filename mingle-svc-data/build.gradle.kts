plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleCore)
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-aop")
    testImplementation("org.postgresql:postgresql:42.7.3")
}

mavenPublishing {
    pom {
        name.set("Mingle Service Data")
        description.set("Based on spring data jpa, it provides DAO architecture.")
        url.set("https://github.com/minguanqiu/mingle-svc-data")
    }
}
