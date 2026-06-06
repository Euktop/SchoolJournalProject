plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    id("org.openapi.generator") version "7.22.0"
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/specs/swagger.json")
    outputDir.set("$buildDir/generated/openapi")
    apiPackage.set("com.schooljournal.api")
    modelPackage.set("com.schooljournal.model")
    configOptions.set(
        mapOf(
            "serializationLibrary" to "moshi",
            "useCoroutines" to "true",
            "dateLibrary" to "java8-localdatetime",
            "enumPropertyNaming" to "UPPERCASE"
        )
    )
}

sourceSets {
    main {
        java.srcDir("$buildDir/generated/openapi/src/main/kotlin")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.logging.interceptor)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
}