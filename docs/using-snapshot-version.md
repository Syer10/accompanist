# Using a Snapshot Version of the Library

If you would like to depend on the cutting edge version of the Accompanist
library, you can use the [snapshot versions][snap] that are published to
[Sonatype OSSRH](https://central.sonatype.org/)'s snapshot repository. These are updated on every commit to `main`.

To do so:

```kotlin
repositories {
    // ...
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    // Check the latest SNAPSHOT version from the link above
    implementation("ca.gosyer:accompanist-pager:XXX-SNAPSHOT")
}
```

 [snap]: https://oss.sonatype.org/content/repositories/snapshots/com/google/accompanist/