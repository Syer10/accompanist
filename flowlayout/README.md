# Jetpack Compose Flow Layouts

[![Maven Central](https://img.shields.io/maven-central/v/ca.gosyer/accompanist-flowlayout)](https://search.maven.org/search?q=g:com.google.accompanist)

Flow layouts adapted from the [Jetpack Compose][compose] alpha versions.

Unlike the standard Row and Column composables, these lay out children in multiple rows/columns if they exceed the available space.

## Usage

``` kotlin
FlowRow {
    // row contents
}

FlowColumn {
    // column contents
}
```

For examples, refer to the [samples](https://github.com/google/accompanist/tree/main/sample/src/main/java/com/google/accompanist/sample/flowlayout).

For more information, visit the documentation: https://google.github.io/accompanist/flowlayout

## Download

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("ca.gosyer:accompanist-flowlayout:<version>")
}
```

[compose]: https://developer.android.com/jetpack/compose