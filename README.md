# redux-frame

A [re-frame](https://github.com/Day8/re-frame) application designed to try out redux tools with re-frame.

The initial setup was built by running:

```
lein new re-frame redux-frame +aliases
```

### Added for redux tools:
The db has a set of values which can be added to.

On load a redux helper is connected to sync the actions to the redux tools.

A serialized copy of the db is added to the redux tools state and on time-travel this is used as the new db.

This way ClojureScript type stay preserved on time-travelling.



### Run application:

```
lein dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein build
```
