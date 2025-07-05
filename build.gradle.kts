val generatedResourcesDir = "${layout.buildDirectory.get()}/generated-resources"

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.4.0"
    id("org.liquibase.gradle") version "2.2.0"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.8.0"
}

group = "com.loc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("mysql:mysql-connector-java:8.0.33")
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Micrometer Prometheus for /actuator/prometheus endpoint
	implementation("io.micrometer:micrometer-registry-prometheus")
    
	// SpringDoc OpenAPI for API docs only (no UI)
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Liquibase dependencies
	implementation("org.liquibase:liquibase-core")
	liquibaseRuntime("org.liquibase:liquibase-core")
	liquibaseRuntime("mysql:mysql-connector-java:8.0.33")
	liquibaseRuntime("org.springframework.boot:spring-boot-starter-data-jpa")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
}

// OpenAPI Generator
openApiGenerate {
	inputSpec.set("$rootDir/src/main/resources/static/openapi.yaml")
	generatorName.set("kotlin-spring")
    modelPackage.set("com.loc.authservice.model")
    outputDir.set("$generatedResourcesDir/openapi")

    configOptions.put("useSpringBoot3", "true")
    configOptions.put("dateLibrary", "java8")
    configOptions.put("delegatePattern", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("modelMutable", "true")
    configOptions.put("useTags", "true")
    configOptions.put("enumPropertyNaming", "UPPERCASE")
    configOptions.put("useBigDecimal", "true")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

sourceSets {
    main {
        kotlin {
            srcDir("$generatedResourcesDir/openapi/src/main/kotlin")
        }
    }
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

liquibase {
    activities {
        register("main") {
            arguments = mapOf(
                "url" to "jdbc:mysql://localhost:3308/auth_service",
                "username" to "root",
                "password" to "mysql",
                "driver" to "com.mysql.cj.jdbc.Driver",
                "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.xml"
            )
        }
    }
}
