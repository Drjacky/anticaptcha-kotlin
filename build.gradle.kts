import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-logging:commons-logging:1.2")
    implementation("org.apache.httpcomponents:fluent-hc:4.5.13")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpclient-cache:4.5.13")
    implementation("org.apache.httpcomponents:httpclient-osgi:4.5.13")
    implementation("org.apache.httpcomponents:httpcore:4.4.15")
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    implementation("org.json:json:20220320")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}