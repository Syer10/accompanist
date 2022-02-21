import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL
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
    // id("com.diffplug.spotless") version "5.14.0"
    kotlin("multiplatform") version "1.6.10" apply false
    id("com.android.library") version "7.0.4" apply false
    id("com.android.application") version "7.0.4" apply false
    id("org.jetbrains.compose") version "1.1.0" apply false
    id("com.vanniktech.maven.publish") version "0.18.0"
    id("org.jetbrains.dokka") version "1.6.10"
    id("me.tylerbwong.gradle.metalava") version "0.2.1" apply false
    id("com.github.ben-manes.versions") version "0.41.0"
}

tasks.withType<DokkaMultiModuleTask> {
    outputDirectory.set(rootProject.file("docs/api"))
    failOnWarning.set(true)
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

subprojects {
    apply(plugin = "com.vanniktech.maven.publish")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "me.tylerbwong.gradle.metalava")
    //apply(plugin = "com.diffplug.spotless")
    //spotless {
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
    //}

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true
        }
    }

    plugins.withType<JavaPlugin> {
        configure<JavaPluginExtension> {
            targetCompatibility = JavaVersion.VERSION_11
            sourceCompatibility = JavaVersion.VERSION_11
        }
    }

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

    tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
        dokkaSourceSets.configureEach {
            reportUndocumented.set(true)
            skipEmptyPackages.set(true)
            skipDeprecated.set(true)
            jdkVersion.set(11)

            // Add Android SDK packages
            noAndroidSdkLink.set(false)

            // Add samples from :sample module
            //samples.from(rootProject.file("sample/src/main/java/"))

            // AndroidX + Compose docs
            externalDocumentationLink {
                url.set(URL("https://developer.android.com/reference/"))
                packageListUrl.set(URL("https://developer.android.com/reference/androidx/package-list"))
            }
            externalDocumentationLink {
                url.set(URL("https://developer.android.com/reference/kotlin/"))
                packageListUrl.set(URL("https://developer.android.com/reference/kotlin/androidx/package-list"))
            }

            sourceLink {
                localDirectory.set(project.file("src/jvmMain/kotlin"))
                // URL showing where the source code can be accessed through the web browser
                remoteUrl.set(URL("https://github.com/Syer10/accompanist/blob/main/${project.name}/src/main/java"))
                // Suffix which is used to append the line number to the URL. Use #L for GitHub
                remoteLineSuffix.set("#L")
            }
        }
    }

    plugins.withType<com.android.build.gradle.BasePlugin> {
        configure<com.android.build.gradle.BaseExtension> {
            compileSdkVersion(31)
            defaultConfig {
                minSdk = 21
                targetSdk = 31

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility(JavaVersion.VERSION_11)
                targetCompatibility(JavaVersion.VERSION_11)
            }
            sourceSets {
                named("main") {
                    val altManifest = file("src/androidMain/AndroidManifest.xml")
                    if (altManifest.exists()) {
                        manifest.srcFile(altManifest.path)
                    }
                }
            }
        }
    }
    plugins.withType<me.tylerbwong.gradle.metalava.plugin.MetalavaPlugin> {
        configure<me.tylerbwong.gradle.metalava.extension.MetalavaExtension> {
            filename = "api/current.api"
            reportLintsAsErrors = true
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