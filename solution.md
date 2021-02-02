# Solution: {github-tangxiaocheng}

Date: 12/14/2020

## Summary

The overall architecture is based on this Repo: <https://github.com/bufferapp/android-clean-architecture-boilerplate> , to make it more practical for this take-home practice, I trimmed a lot of features, and kept the essential clean architecture framework. 
* It uses repository to provide single source of truth for data and handles the server error with unit test.
* It uses use-cases to handle business logic. 
* It uses presenter to connect the use-cases and the view.
* It makes Activity code as simple as possible, no if-else statement at all.
* For sending a network request in a activity, it has this three results:
  * onResultReady: the server returns the data as expected.
  * onServerError: the server couldn't handle current request properly, it provides a button for the user to retry.
  * onNetworkError: the connection is lost, most likely the network inner the app is down.
* It uses sealed class to wrap the network responses.
* It uses AutoDispose to manage RxJava stream subscription.  
* It uses [Scabbard](https://arunkumar9t2.github.io/scabbard/) to visualize Dagger 2 dependency graphs as the below image.
![Dagger Graph](doc/10_di_graph.png?raw=true "Dagger Graph")

### App launch with network connected.
![Normal](doc/1_normal.gif?raw=true "Normal")
### App launch with network off.
![Network Down](doc/4_network_down.gif?raw=true "Network Down")
### App screen rotation.
![Screen rotate](doc/3_screen_rotate.gif?raw=true "Screen rotate")
### Click item to show detail.
![To detail](doc/2_list_to_detail.gif?raw=true "To detail")

For more detail, please refer to comment inside the code.

## Requirements

You could install the app using the following command.
```
./gradlew :app:installDebug
```

## Ambiguity Notes

For the time being, the UI is not well polished. I would gladly optimize it based on the requirement from the UI designer.

