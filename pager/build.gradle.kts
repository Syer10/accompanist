/*
 * Copyright 2021 The Android Open Source Project
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
    kotlin("jvm")
    `java-library`
    id("org.jetbrains.compose")
    id("me.tylerbwong.gradle.metalava")
}

kotlin {
    explicitApi()
}

dependencies {
    api(compose.foundation)
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.5")

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

metalava {
    filename = "api/current.api"
    reportLintsAsErrors = true
}