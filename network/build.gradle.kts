plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    id("org.openapi.generator") version "7.21.0"
}
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/specs/swagger.json")
    outputDir.set("$buildDir/generated/openapi")
    apiPackage.set("com.schooljournal.api")
    modelPackage.set("com.schooljournal.model")
    configOptions.set(mapOf(
        "serializationLibrary" to "moshi",
        "useCoroutines" to "true",
        "dateLibrary" to "java8-localdatetime",
        "enumPropertyNaming" to "UPPERCASE"
    ))
}

// Переносим условие в саму задачу
tasks.named("openApiGenerate") {
    onlyIf { file("$projectDir/specs/swagger.json").exists() }
    dependsOn("downloadSwagger")
}

sourceSets {
    main {
        java.srcDir("$buildDir/generated/openapi/src/main/kotlin")
    }
}

tasks.register("downloadSwagger", Exec::class) {
    val swaggerUrl = "https://localhost:7191/swagger/v1/swagger.json"
    val outputFile = "$projectDir/specs/swagger.json"
    commandLine("curl", "-k", "-o", outputFile, swaggerUrl)
    isIgnoreExitValue = true
    doFirst {
        mkdir("$projectDir/specs")
    }
    doLast {
        val f = file(outputFile)
        if (!f.exists() || f.length() == 0L) {
            f.delete()
            println("Swagger not downloaded (no connection or error). Will use existing file if present.")
        }
    }
}

// Остальные зависимости компиляции — без изменений
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("openApiGenerate")
}
tasks.matching { it.name.startsWith("ksp") }.configureEach {
    dependsOn("openApiGenerate")
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
    implementation(libs.mockwebserver)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.javax.inject)
    testImplementation(libs.kotlinx.coroutines.test)
}