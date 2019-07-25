import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.1.5.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.30" apply false
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.30" apply false
    kotlin("jvm") version "1.3.30"
    kotlin("kapt") version "1.3.30"
    `java-library`
}

subprojects {

    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "idea")

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    dependencies {
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-jdk8"))
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "junit")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-parameters")
            jvmTarget = "1.8"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project(":manitto") {
    extra["querydslVersion"] = "4.2.1"
    group = "org.frienitto.manitto"

    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-kapt")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-mail")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.querydsl:querydsl-jpa:${extra["querydslVersion"]}")
        // https://mvnrepository.com/artifact/io.springfox/springfox-swagger2
        implementation("io.springfox:springfox-swagger2:2.9.2")
        implementation("io.springfox:springfox-swagger-ui:2.9.2")
        
        kapt("com.querydsl:querydsl-apt:${extra["querydslVersion"]}:jpa")
        kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

        runtimeOnly("com.h2database:h2")
        runtimeOnly("mysql:mysql-connector-java")
    }
}

project(":apigw") {
    group = "org.frienitto.apigw"

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-undertow")
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-zuul")
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
        }
    }
}
