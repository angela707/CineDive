# 🍿 CineDive - Movie & TV Show Explorer

CineDive is an Android application built with Kotlin and Jetpack Compose. It allows users to browse and search for movies and TV shows using The Movie Database (TMDb) API. The app supports pagination, offline caching with Room, and dependency injection using Hilt.

## ⚙️ Getting Started
### Prerequisites

1. **Clone the repository**:
2. **Open the project** in Android Studio.
3. **Set up TMDb API Key**:
   - Generate an API key from [TMDb](https://www.themoviedb.org/).
   - Create a properties file called `apikeys.properties`:
   - Add your token to the file
     ```sh
     api.token="your_api_key"
     ```
4. **Run the project** on an emulator or physical device.

## 🎥 Features

- 📜 **Infinite Scrolling & Pagination**: Efficiently loads data using Jetpack Paging 3 for a seamless browsing experience.
- 📌 **Detailed Movie & TV Show Information**: View essential details like title, poster, rating, overview, and release date.
- 📡 **Offline Mode**: Cached data using Room database allows access even without an internet connection.
- 🔍 **Search Functionality**: Search movies or TV shows with instant results and API request optimization.
- 🖌️ **Modern UI**: Built using Jetpack Compose for a smooth and interactive interface.

## 🏛️ Architecture

- **MVVM Pattern**: Ensures separation of concerns, making the codebase scalable and maintainable.
- **Clean Architecture**: Divided into layers for better code organization and testability.
- **Sealed Classes & Enums**: Used for efficient state and error management.

## 🌐 Networking

- **Retrofit & OkHttp**: Handles API requests and responses efficiently.
- **Timber Logging**: Used for debugging network requests and errors.

## 📊 Testing

- **Unit Testing**: Ensures reliability of ViewModels and repositories.
- **MockK**: Used for mocking dependencies in tests.

## 📁 Folder Structure

```
├── data
│   ├── local (Room database and DAO)
│   ├── remote (Retrofit API, DTOs, Paging Sources)
│   ├── repository (Data handling, caching, fetching)
│
├── domain
│   ├── models (Movie & TV Show data models)
│   ├── repository (Interface for data abstraction)
│   │── utils (Utility functions to aid with mapping)
│
├── presentation
│   ├── components (Jetpack Compose components used through the screens)
│   ├── models (UiStates to represent the UI)
│   ├── ui (Colors, themes, types)
│   ├── viewmodel (Business logic for UI interaction)
│   ├── screens (Represent the different screens in the app)
│
├── di (Hilt Dependency Injection setup)
```
