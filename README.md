# Cat Catalogue - Android App
This is a portfolio project that demonstrates the use of Compose, Material 3, Coroutines including Flows, Hilt, Jetpack Navigation, ViewModel, MVI and Clean Architecture.

### App Functionality
A catalogue of cat breeds with detailed information about each breed and photos

## Screen Shots

| Home Screen | Detail Info | Detail Images |
|     :---:     |     :---:     |     :---:     |
|<img src="https://i.imgur.com/ByVPrPq.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/MuYaZLv.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/EeVhB2c.png" width="225px" height="60%" align="centre">|

| Home Screen | Detail Info | Detail Images |
|     :---:     |     :---:     |     :---:     |
 |<img src="https://i.imgur.com/oGLftw8.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/XVeSqNA.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/Qm8y7pB.png" width="225px" height="60%" align="centre">|

### Screen Shots With Maximum Font Size and Display Size
 | Home Screen | Detail Info Top | Detail Info Bottom | Detail Images |
|     :---:     |     :---:     |     :---:     |     :---:     |
 |<img src="https://i.imgur.com/rnxT0fO.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/pcI03Oa.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/K63VyEr.png" width="225px" height="60%" align="centre">|<img src="https://i.imgur.com/lCdiqA2.png" width="225px" height="60%" align="centre">|
 
### Animations
| Home Screen |
|     :---:     |
![Alt Text](https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExczZkdzEwb3l4NXB6a2I1eWhxcmV1MXRybGVqZ290NWl5OG5mN2tjbSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/zC9KXUsYGZolVRGD8h/giphy.gif)

## Technical Information
### Presentation Layer
The app is built in MVI, where each UI 'screen' has its own ViewModel, which exposes a single StateFlow containing the entire view state. Each ViewModel is responsible for subscribing to any data streams and objects required for the view, as well as exposing functions which allow the UI to send events.

Using the HomeScreen as an example within the <code>[com.tinnovakovic.catcataloguer.presentation.home](https://github.com/TinNova/CatCataloguer/tree/master/app/src/main/java/com/tinnovakovic/catcataloguer/presentation/home)</code> package:

- The ViewModel is implemented as <code>[HomeViewModel](https://github.com/TinNova/CatCataloguer/blob/master/app/src/main/java/com/tinnovakovic/catcataloguer/presentation/home/HomeViewModel.kt)</code>, which exposes a MutableStateFlow<HomeContract> for the UI to observe.
- <code>[HomeContract](https://github.com/TinNova/CatCataloguer/blob/master/app/src/main/java/com/tinnovakovic/catcataloguer/presentation/home/HomeContract.kt)</code> contains the complete view state for the home screen as an @Immutable data class ```UiState()```. It also exposes the functions which enable the UI to send events to the ViewModel in the form of a sealed class ```sealed class UiEvents : BaseUiEvent```.
- The Compose <code>[HomeScreen](https://github.com/TinNova/CatCataloguer/blob/master/app/src/main/java/com/tinnovakovic/catcataloguer/presentation/home/HomeScreen.kt)</code> uses HomeViewModel, and observes it's UiState as Compose State, using ```collectAsStateWithLifecycle()```:
```
val viewModel = hiltViewModel<HomeViewModel>()
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```
This MVI pattern is made scalable by inheriting the base classes in <code>[com.tinnovakovic.catcataloguer.shared.mvi](https://github.com/TinNova/CatCataloguer/tree/master/app/src/main/java/com/tinnovakovic/catcataloguer/shared/mvi)</code>, ```BaseUiEvent```, ```BaseUiState``` and ```BaseViewModel```

### App Architecture
The app uses Clean Architecture. 
- The Data layer integrates Networking sources from Retrofit and Persistent sources from Room using the Repository pattern and merges them together using the Paging 3 library to fetch paginated data from the API and Room, furthermore it exposes UI models mapped from the Network and Persistent models which are incapsulated within the Data layer.
- The Domain layer manages business logic using the UseCase pattern.
- The Presentation layer uses the MVI pattern as described above.

### Pagination
To solve pagination the Paging 3 library was used, this handling paginating the API data and paginating the persisted data as well, this ensures that the application only fetches a manageble amount of data at once and that data is still available when offline.

### Accessibility
To ensure the app is accessibile to all users I've put focus on ensuring the app looks and functions well when the font and the screen zoom is at the largest system settings, I encourage you to try it.

## Tech-Stack

* Kotlin
* Dagger Hilt
* Coroutines
* Compose
* Architecture
  * Clean Architecture
  * MVI
* Jetpack Navigation 
* Testing
  * JUnit5
  * Mockk

## To Run The App
You need to get your own APIKEY from here https://thecatapi.com/ and insert it into your local.properties file:

```
THE_CAT_API_API_KEY = {API_KEY}
```
