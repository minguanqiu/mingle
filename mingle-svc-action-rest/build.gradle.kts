plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleSvcAction)
    api("com.squareup.okhttp3:okhttp:4.12.0")
}

mavenPublishing {
    pom {
        name.set("Mingle Service Action For RESTful")
        description.set("One of the implementations of action, providing RESTful client functions.")
        url.set("https://github.com/minguanqiu/mingle-svc-action-rest")
    }
}
