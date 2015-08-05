# PopMovies


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
3. 
