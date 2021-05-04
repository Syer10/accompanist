import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.diffplug.spotless") version "5.9.0"
    kotlin("jvm") version "1.4.32" apply false
    id("org.jetbrains.compose") version "0.4.0-build184" apply false
    id("com.vanniktech.maven.publish") version "0.15.1"
    id("org.jetbrains.dokka") version "1.4.30"
    id("me.tylerbwong.gradle.metalava") version "0.1.6" apply false
}


repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

tasks.withType<DokkaMultiModuleTask> {
    outputDirectory.set(rootProject.file("docs/api"))
    failOnWarning.set(true)
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}
subprojects {
    apply(plugin = "com.vanniktech.maven.publish")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.diffplug.spotless")
    spotless {
        //kotlin {
        //    target("**/*.kt")
        //    ktlint("0.40.0")
        //    licenseHeader(rootProject.file("spotless/copyright.txt").absolutePath)
        //}

        //kotlinGradle {
        //    target("**/*.gradle.kts")
        //    ktlint("0.40.0")
        //    licenseHeader(rootProject.file("spotless/copyright.txt").absolutePath, "(buildscript|apply|import|plugins)")
        //}
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            // Set JVM target to 11
            jvmTarget = "11"

            freeCompilerArgs = listOf(
                // Allow use of @OptIn
                "-Xopt-in=kotlin.RequiresOptIn",
                // FIXME figure out why this is needed
                "-Xallow-unstable-dependencies"
            )
        }
    }

    val mavenPublish = extensions["mavenPublish"] as MavenPublishPluginExtension
    mavenPublish.sonatypeHost = SonatypeHost.S01


    // Read in the signing.properties file if it is exists
    val signingPropsFile = rootProject.file("release/signing.properties")
    if (signingPropsFile.exists()) {
        Properties().apply {
            signingPropsFile.inputStream().use {
                load(it)
            }
        }.forEach { key1, value1 ->
            val key = key1.toString()
            val value = value1.toString()
            if (key == "signing.secretKeyRingFile") {
                // If this is the key ring, treat it as a relative path
                project.ext.set(key, rootProject.file(value).absolutePath)
            } else {
                project.ext.set(key, value)
            }
        }
    }

    // Must be afterEvaluate or else com.vanniktech.maven.publish will overwrite our
    // dokka and version configuration.
    afterEvaluate {

        /*
        val composeSnapshot = libs.versions.composesnapshot.get()
        if (composeSnapshot.length() > 1) {
            // We"re depending on a Jetpack Compose snapshot, update the library version name
            // to indicate it"s from a Compose snapshot
            val versionName = project.properties.get("VERSION_NAME")
            if (versionName.contains("SNAPSHOT")) = {
                version = versionName.replace("-SNAPSHOT", = ".compose-${composeSnapshot}-SNAPSHOT")
            }
        }*/

        if (!version.toString().endsWith("SNAPSHOT")) {
            // If we"re not a SNAPSHOT library version, we fail the build if we"re relying on
            // any snapshot dependencies
            configurations.configureEach {
                dependencies.configureEach a@{
                    if (this is ProjectDependency) return@a
                    val depVersion = this.version
                    if (depVersion != null && depVersion.endsWith("SNAPSHOT")) {
                        throw IllegalArgumentException(
                            "Using SNAPSHOT dependency with non-SNAPSHOT library version: $this"
                        )
                    }
                }
            }
        }
    }
}
