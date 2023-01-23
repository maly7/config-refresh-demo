plugins {
	java
	id("org.springframework.boot") version "2.4.13"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.github.maly7"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

extra["springCloudVersion"] = "2020.0.6"
extra["springCloudServicesVersion"] = "3.2.1.RELEASE"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("io.pivotal.spring.cloud:spring-cloud-services-starter-config-client")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("io.pivotal.spring.cloud:spring-cloud-services-dependencies:${property("springCloudServicesVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

springBoot {
	buildInfo {
		properties {
			additional = mapOf(
				"spring.boot.version" to "2.4.13"
			)
		}
	}
}