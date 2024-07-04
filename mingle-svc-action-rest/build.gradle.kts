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
        description.set("One of the implementations of action, providing RESTful client functions.")
    }
}