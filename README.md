# Story

Using the environment and tools of your choice, write a native mobile application that
lists all the bike shops within a 100 mile radius from the location of your choice.

## Minimum Acceptance Criteria

* The app should have a “Home” view that displays the starting location the app is using
* The app should have a a list view showing the location results
* The app should lazy load the location results, and fetch 10 location results at a time
* Each list view item should show at a minimum:
* A thumbnail image of the location
* The location name
* The list view should be sorted by proximity to the starting location, closest first
* Tapping on a list view item should navigate to a detail view
* The detail view should show at a minimum:
* Location image
* Location name
* Location address
* The app UI should look the same across all form factors

Date: 02/02/2020
## Summary 

The overall architecture is based on this Repo: <https://github.com/bufferapp/android-clean-architecture-boilerplate> , to make it more practical for this take-home practice, I trimmed a lot of features, and kept the essential clean architecture framework. 
* It uses repository to provide single source of truth for data and handles the server error with unit test.
* It uses use-cases to handle business logic. 
* It uses presenter to connect the use-cases and the view.
* It makes Activity code as simple as possible, no if-else statement at all.
* It uses sealed class to wrap the network responses.
* It uses AutoDispose to manage RxJava stream subscription.  
* It uses [Scabbard](https://arunkumar9t2.github.io/scabbard/) to visualize Dagger 2 dependency graphs as the below image.
![Dagger Graph](doc/10_di_graph.png?raw=true "Dagger Graph")

## Requirements

You could install the app using the following command.
```
./gradlew :app:installDebug
```
### APK download
<https://github.com/tangxiaocheng/SramBikeStore/blob/Document/doc/app-debug.apk>

### For app preview
<https://github.com/tangxiaocheng/SramBikeStore/pull/1>
