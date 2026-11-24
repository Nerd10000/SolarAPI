plugins {
    kotlin("jvm") version "2.4.0"
    `java-library`
    `maven-publish`
    signing
}

group = "dragon.me"
version = "1.0.0"
base {
    archivesName.set("solar-api")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    api("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
    compilerOptions {
        javaParameters.set(true)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("api") {
            from(components["java"])

            groupId = "dragon.me"
            artifactId = "solar-api"
            version = project.version.toString()

            pom {
                name.set("Solar API")
                description.set("Public API for the Solar Minecraft plugin")
                url.set("https://github.com/Nerd10000/Solar")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("nerd10000")
                    }
                }

                scm {
                    url.set("https://github.com/Nerd10000/Solar")
                    connection.set(
                        "scm:git:git://github.com/Nerd10000/Solar.git"
                    )
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            url = uri(
                "https://central.sonatype.com/api/v1/publisher/upload"
            )

            // This URL is NOT a Maven deployment endpoint.
            // Use the Sonatype Central Publishing Gradle plugin
            // or its supported publishing integration instead.
        }
    }
}