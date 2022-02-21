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
                kotlinOptions.jvmTarget = "11"
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api(compose.material)
                api(project(":placeholder"))
                implementation(libs.androidx.annotation)
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
            kotlin.srcDir("src/desktopMain/kotlin")
            dependencies {
                api(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            kotlin.srcDir("src/desktopTest/kotlin")
        }

        val androidMain by getting {
            kotlin.srcDir("src/jvmMain/kotlin")
            dependencies {
                api(kotlin("stdlib-jdk8"))
            }
        }
        val androidTest by getting {
            kotlin.srcDir("src/jvmTest/kotlin")
        }
    }
}
