plugins {
    id ("java")
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ru.reworlds"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.inventivetalent.org/repository/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    implementation("org.json:json:20220320")
    implementation("net.sf.trove4j:trove4j:3.0.3")
    implementation("org.mineskin:java-client:1.2.0-SNAPSHOT")
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<Jar> {
    manifest.attributes["Main-Class"] = "example.SomeClazz"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}