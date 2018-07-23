# Popular_Movies

This is the stage two implementation of Popular Movies Design of Udacity Android Developer Nanodegree. This app fetches movies data from TMDb API, sort data besed on "top rated" and "most popular" and also store users' favorite in local database.
It displays the related movie details whenever clicking on one movie poster. It launches the overview, reviews and trailers of each movie and allows user to watch the trailers.

## Features
Within this app, you could
* Sort and display movies based on "top rated" or "most popular" 
* Watch movie rating, reviews and trailers
* Add movie as favorite

## Screens
<div>
 <img width="40%" src="../master/screens/top_rated.jpg" />
 <img width="40%" src="../master/screens/favorite.jpg" />
 </div>
<img width="40%" src="../master/screens/details.png" />

## Getting Started

 1. Apply for your own API key in TMDb website: https://www.themoviedb.org/account/signup?language=en-EN. 
 2. Then paste you API key into "gradle.properties" file.
 3. Run this app.
 
## Libraries

* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Retrofit](https://github.com/square/retrofit)
* [Picasso](https://github.com/square/picasso)

## Authors

* **Bo Han** 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

   Copyright 2018 Bo Han

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
