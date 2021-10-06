# GitHub Repositories

## Requirements

To build this project you will need:

* Android Studio (Latest stable version)

## Project Structure

This project uses MVI architecture, the main structure can be seen bellow

```
.
└── GitHubRepositories/
    ├── app/ // Contains the main module implementation.
    ├── data/ // Contains the remote and offline stored data.
    ├── domain/ // Contains models, usecases and repositories interfaces
    ├── android.gradle // Contains the gradle configuration for all the android modules.
    ├── build.gradle
    ├── detekt.gradle 
    ├── detekt.yml // Contains the configuration for detekt tool.
    ├── README.md
    └── settings.gradle
```

## Dependencies

This project uses [Gradle](https://gradle.org/) for dependency management.

All the dependencies declarations are inside the `libs.versions.toml` module.

A list of the main libraries and dependencies used in this project can be viewed below:

### [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)

Used for asynchronous and non-blocking code.

### [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

We use Dagger hilt as Dependency Injection Framework.

### [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=pt-br)

It helps to create pagination and local cache control.

### [Navigation](https://developer.android.com/guide/navigation)

It helps to create navigation between `Fragments`, `Dialogs`, etc.

### [Room](https://developer.android.com/topic/libraries/architecture/room)

We use Room to store data in the SQLite database.

### [Material](https://material.io/develop/android/docs/getting-started)

This library helps us to create the UI using the Material Design system.

### [Coil](https://github.com/coil-kt/coil)

Responsible for loading and caching images in the app.

### [Shimmer for Android](https://github.com/facebook/shimmer-android)

Create a place holder animation.

### [Moshi](https://github.com/square/moshi)

This library is used to parse JSON into Kotlin/Java objects.

### [Retrofit](https://square.github.io/retrofit/)

The HTTP clint used in the app to consume the API of this project.
