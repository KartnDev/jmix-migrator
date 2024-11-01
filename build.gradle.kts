plugins {
    kotlin("jvm") version "2.0.10"
}

group = "io.kartondev"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.26.2")
    implementation("fr.inria.gforge.spoon:spoon-core:10.4.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}