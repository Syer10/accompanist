import org.jetbrains.compose.compose

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
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    explicitApi()

    android {
        publishAllLibraryVariants()
        compilations {
            all {
                kotlinOptions.jvmTarget = "1.8"
            }
        }
    }
    jvm {
        compilations {
            all {
                kotlinOptions.jvmTarget = "11"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(compose.ui)
                compileOnly(compose.animation)
                implementation(compose("org.jetbrains.compose.ui:ui-util"))
                implementation(libs.napier)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting

        val androidMain by getting {
            dependencies {
                compileOnly(libs.compose.animation.animation)
                compileOnly(libs.compose.ui.ui)
            }
        }
        val androidUnitTest by getting

        val darwinMain by creating {
            dependsOn(commonMain)
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }

        listOf(
            "iosX64",
            "iosArm64",
            "iosSimulatorArm64",
            "macosX64",
            "macosArm64"
        ).forEach {
            getByName(it + "Main").dependsOn(darwinMain)
            getByName(it + "Test").dependsOn(darwinTest)
        }
    }
}

android {
    namespace = "com.google.accompanist.placeholder"
}