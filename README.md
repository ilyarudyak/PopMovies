# PopMovies
We document here some implementation details primarily related to the stage 2 of the project.

## User interface
1. We allow user to choose UI for `PosterFragment` from given in the rubric options including favorities. We have two implementations of Favorities (only on a phone, on a tablet we use only the first one):
  * `Favorities (API)`. In the first implementation we store favorite movies id in `SharedPreference`. So when a user clicks the favorities button we add the movies id into set from `SharedPreference`. We also check the list of movies when rendering layout of detail fragment and the favorities button is disabled for movies in this list. When a user clicks on this option in the menu we make new API calls for each movie using its id (this API call is not mentioned in the project description: https://api.themoviedb.org/3/movie/135397?api_key=..., where 135397 is the movie id).
  * `Favorities (DB)`. Here we use our implementation of content provider (see below).
2. We also implemented shared intent to share first trailer in the list. Before trying to load poster images we added check of internet connection. We also implemented `ViewHolder` pattern in our adapter.

## Movie class
1. We encapsulate movie details into Movie class. We also create `Trailer` class as an inner class of `Movie`. It implements `Parcelable` interface to be stored into bundle. We store reviews just as `List<String>`. In a production app we will probably encapsulate them in class as well.


## Tablet 
### Tablet layout
1. We allow only landscape layout for a tablet. Portrait layout probably can be implemented the same as on a phone but we still have to adjust poster sizes and columns etc. We use two version of `bools.xml` and `getBoolean()` method in `MainActivity` for this purpose. 
2. On a tablet we use static + dynamic fragments for main activity (for `PosterFragment` and `DetailFragment`). On a phone we had a static detail fragment. Now it's also dynamic. To define layout we check if we have a container for dynamic fragment. We also define member variable `mTwoPane` that is used in callback implementation (see below).
3. We have 3 columns of posters only on Nexus 7 (480 dp for fragment width with 1:1 weights for two fragments). We use `android:columnWidth="160dp" and android:numColumns="auto_fit"` to get this number of columns. On other tablets it can be different.
4. We use action bar menu only from poster fragment (we do not have menu in main activity, detail activity and detail fragment). We do not implement content provider functionality on a tablet so we do not have `Favorities (db)` item in menu on a tablet. 
5. To remove horizontal spacing between images on a tablet we use `imageView.setAdjustViewBounds(true)` in our `PicassoAdapter class`. 

### Callback
1. Code for callback implementation is taken from [android site](http://developer.android.com/training/basics/fragments/communicating.html). It's basically the same that is used in Sunshine. We use Movie as a parameter in callback method. We call `mCallback.onPosterSelected(mMovie)` inside `onPostExecute()` method of our background task to fetch trailers and reviews. 
2. We use `mTwoPane` member variable in callback implementation to distinguish between one and two pane layouts. In case of a phone layout we send intent to detail activity as usual. In case of a tablet layout we create new detail fragment and replace the existing one.
3. We refactor intent to package all data into bundle and build intent with `putExtra(BUNDLE, bundle)`. Than in `DetailActivity` we use this bundle to set detail fragment arguments `df.setArguments(bundle)`.

## Content provider
1. We create content provider for database of popular movies. We may add or delete a movie or slect all movies and show its posters. We don't use any libraries and create it from scratch.
2. We insert and delete a movie upon click on a favorite button. We insert a movie using `Bundle` object and helper method `Movie.buildContentValuesFromBundle()`. We delete a movie using its `id` and sql `WHERE tmdb_id = id`.
3. We create custom `CursorAdapter` that is similar to our `PicassoAdapter`, in particular we fetch posters using `Picasso`. We don't store any posters on a device. In case of no internet connection we just get an error message: `no internet connection`. 
4. We use CursorLoader to get information about posters from database in `FavPosterFragment` and all other information in `FavDetailFragment`. An alternative solution would be use `AsynctaskLoader<List<Movie>>` - we probably can use `PicassoAdapter` in this case. We use `AsynTask` to add or remove a movie from database.
5. We create new activities to show favorites and their details just not to get overcomplicated fragments. We also have different logic of calling `DetailFragment` - we use `Bundle` and `Uri` in these cases.
