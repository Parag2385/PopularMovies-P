# Popular Movies App

![Travis-ci](https://travis-ci.org/Parag2385/PopularMovies-P.svg?branch=master)

A simple Android app, that helps user to discover movies. This is Project 1 & Project 2 of Udacity's Android Developer Nanodegree.

### Features:

  - Discover the most popular, the highest rated and the most rated movies
  - Watch movie trailers and teasers
  - Read reviews from other users
  - Mark movies as favorites
  - Offline work
  - Material design

## Screenshots

<img src="Screens/Screenshot_20171215-132915.jpg" width="45%">
<img src="Screens/Screenshot_20171215-141328.jpg" width="45%">
<img src="Screens/Screenshot_20171215-141614.jpg" width="90%">
<img src="Screens/Screenshot_20171215-145454.jpg" width="90%">

## Libraries

* [Picasso] - A powerful image downloading and caching library for Android
* [Moshi] - A modern JSON library for Android and Java.

Developer setup
---------------
### Requirements

  - Java 8
  - Latest version of Android SDK and Android Build Tools

### API Key

The app uses themoviedb.org API to get movie information and posters. You must provide your own [API key][1] in order to build the app.

Just put your API key into `~/.gradle/gradle.properties` file (create the file if it does not exist already):

```gradle
API_KEY="wxyz"
```

### Building
You can build the app with Android Studio or with ./gradlew assembleDebug command.


License
-------

    Copyright 2017 Parag Pawar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[Picasso]: <http://square.github.io/picasso/>
[Moshi]: <http://square.github.io/moshi/1.x/moshi/>
