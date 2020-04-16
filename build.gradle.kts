plugins {
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    application
}

repositories {
    jcenter()
    mavenCentral()
    google()
    maven { setUrl("http://jasperreports.sourceforge.net/maven2/") }
    maven { setUrl("http://jaspersoft.artifactoryonline.com/jaspersoft/third-party-ce-artifacts/") }

}

javafx {
    version = "14"
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.3.5")
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation("net.sf.jasperreports:jasperreports:6.12.2")
    implementation("net.sf.jasperreports:jasperreports-functions:6.12.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("de.jensd:fontawesomefx:8.9")
    implementation("pl.allegro.finance:tradukisto:1.8.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "net.decodex.invoice.App"
}
