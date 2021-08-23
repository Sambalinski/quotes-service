import Build_gradle.Directories.genDir
import Build_gradle.Versions.postgresVersion
import Build_gradle.Versions.swagger2Version
import com.rohanprabhu.gradle.plugins.kdjooq.database
import com.rohanprabhu.gradle.plugins.kdjooq.generate
import com.rohanprabhu.gradle.plugins.kdjooq.generator
import com.rohanprabhu.gradle.plugins.kdjooq.jdbc
import com.rohanprabhu.gradle.plugins.kdjooq.jooqCodegenConfiguration
import com.rohanprabhu.gradle.plugins.kdjooq.target
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Property

object Versions {
    const val postgresVersion = "42.2.12"
    const val swagger2Version = "3.0.0"
}

object Directories {
    const val genDir = "src/main/resources/generated"
}

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
    id("dev.bombinating.jooq-codegen") version "1.7.0"
    id("com.rohanprabhu.kotlin-dsl-jooq") version "0.4.6"
}

group = "com.epam.quotesservice"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    all {
        exclude(group = "org.mockito")
    }
}

dependencies {
    // main
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // kotlin json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // db
    implementation("org.postgresql:postgresql:$postgresVersion")
    jooqRuntime("org.postgresql:postgresql:$postgresVersion")
    runtimeOnly("org.postgresql:postgresql:$postgresVersion")
    jooqGeneratorRuntime("org.postgresql:postgresql:$postgresVersion")
    jooqGeneratorRuntime("ch.qos.logback:logback-classic:1.2.3")
    // swagger
    implementation("io.springfox:springfox-boot-starter:$swagger2Version")
    implementation("io.springfox:springfox-swagger2:$swagger2Version")
    implementation("io.springfox:springfox-swagger-ui:$swagger2Version")
    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
    dependsOn(":jooq-codegen-java")
}

jooqGenerator {
    attachToCompileJava = false
    jooqVersion = "3.14.1"
    configuration("xml", project.sourceSets.getByName("main")) {
        configuration = jooqCodegenConfiguration {
            jdbc {
                url = project.properties.getOrDefault(
                    "DB_URL",
                    "jdbc:postgresql://localhost:5432/quotes"
                ).toString()
                username = project.properties.getOrDefault(
                    "DB_USER",
                    "postgres"
                ).toString()
                password = project.properties.getOrDefault(
                    "DB_PASSWORD",
                    "postgres"
                ).toString()
                driver = "org.postgresql.Driver"
            }
            generator {
                name = "org.jooq.codegen.XMLGenerator"
                database {
                    name = "org.jooq.meta.postgres.PostgresDatabase"
                    inputSchema = "public"
                    includes = "quotes | quotes_history"
                }
                target {
                    directory = genDir
                }
            }
        }
    }

    configuration("java", project.sourceSets.getByName("main")) {
        configuration = jooqCodegenConfiguration {
            generator {
                database {
                    name = "org.jooq.meta.xml.XMLDatabase"
                    inputSchema = "public"
                    includes = ".*"
                    properties.add(Property().withKey("dialect").withValue("POSTGRES"))
                    properties.add(
                        Property()
                            .withKey("xmlFile")
                            .withValue("$genDir/org/jooq/generated/information_schema.xml")
                    )
                }
                generate {
                    isGeneratedAnnotation = false
                    isRelations = true
                    isDeprecated = false
                    isRecords = true
                    isPojos = true
                    isPojosEqualsAndHashCode = true
                    isFluentSetters = true
                    isJavaTimeTypes = true
                }
                target {
                    packageName = "com.epam.quotesservice.jooq"
                    directory = "src/main/java"
                }
            }
        }
    }
}

tasks.getByName("cleanJooq-codegen-xml") {
    enabled = false
}

tasks.getByName("cleanJooq-codegen-java") {
    enabled = false
}
