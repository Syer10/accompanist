![Accompanist logo](docs/header.png)

Accompanist is a group of libraries that aim to supplement [Jetpack Compose][compose] with features that are commonly required by developers but not yet available. This version is meant to port some of the Accompanist libraries to Compose Multiplatform, so it can be used on the Jvm and Android.

**Current Library Compose Multiplatform Version: 1.2.0-alpha01-dev774**

**Current Library Jetpack Compose Version: 1.2.1**

Currently, Accompanist contains:
### 📖 [Pager](./pager/)
A library that provides utilities for building paginated layouts in Jetpack Compose, similar to Android's [ViewPager][viewpager].

### ⏳ [Placeholder](./placeholder/)
A library that provides easy-to-use modifiers for displaying a placeholder UI while content is loading.

### 🌊 [Flow Layouts](./flowlayout/)
A library that adds Flexbox-like layout components to Jetpack Compose.

### ⬇️ [Swipe to Refresh](./swiperefresh/)
A library that provides a layout implementing the swipe-to-refresh UX pattern, similar to Android's [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout).

---

## Future?

Any of the features available in this group of libraries may become obsolete in the future, at which point they will (probably) become deprecated. 

We will aim to provide a migration path (where possible), to whatever supersedes the functionality.

---

### Why the name?

The library is all about adding some utilities around Compose. Music composing is done by a
composer, and since this library is about supporting composition, the supporting role of an [accompanist](https://en.wikipedia.org/wiki/Accompaniment) felt like a good name.

## Contributions

Please contribute! We will gladly review any pull requests.
Make sure to read the [Contributing](CONTRIBUTING.md) page first though.

## License

```
Copyright 2020 The Android Open Source Project
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[appcompat]: https://developer.android.com/jetpack/androidx/releases/appcompat
[compose]: https://developer.android.com/jetpack/compose
[viewpager]: https://developer.android.com/reference/kotlin/androidx/viewpager/widget/ViewPager
