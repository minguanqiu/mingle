plugins {
    id("java-library")
}

dependencies {
    api(platform(projects.mingleBom))
    api(projects.mingleSvcAction)
    api("com.squareup.okhttp3:okhttp:4.12.0")
}