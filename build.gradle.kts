plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    application
}

repositories {
    jcenter()
    mavenCentral()
    google()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "rs.decodex.invoice.App"
}
