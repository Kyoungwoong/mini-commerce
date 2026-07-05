plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "mini-commerce"

include("common")
include("commerce-monolith")
