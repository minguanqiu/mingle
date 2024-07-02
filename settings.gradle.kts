plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "mingle"

include("mingle-bom")
include(":mingle-core")
include(":mingle-svc-action")
include(":mingle-svc-action-rest")
include(":mingle-svc-data")
include(":mingle-svc-redis")
include(":mingle-svc-session")


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
