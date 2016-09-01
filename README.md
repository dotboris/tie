tie
===

[![Build Status](https://travis-ci.org/dotboris/tie.svg?branch=master)](https://travis-ci.org/dotboris/tie)

A ClojureScript library that provides data-bound form controls for reagent.

Why
---

Building forms in reagent is pretty tedious. For every control, you need to
define `:value` / `:checked` and `:on-change`. These are always implemented the
same way. They read from an atom and write back to it. Tie solves this by
letting you just pass in the atom and the data binding is automatic.

Usage
-----

Let's start with a quick example:

```clojure
(ns my.core
  (:require [tie.core :as t] ; require tie
            [reagent.core :as r]))

(defonce state (r/atom {}))

(defn my-component [state]
  [:form
    [:label "What is your name?"
      ; use control components
      [t/text {:atom (r/cursor state [:name])}]]
    [:label "Tell me a story."
      ; use more control components
      [t/textarea {:atom (r/cursor state [:story])}]]

    [:button {:type "submit"}]])

(r/render [my-component state]
          (.getElementById js/document "app"))
```

### Components

You create components using the square bracket notation. They take a map of
properties as their first argument. Data binding is done with the `:atom`
property. Extra properties will be passed to the underlying control. The exact
property will vary from one component to the next.

Here is a simple example with a textbox:

```clojure
[t/text {:atom some-atom :class "fancy-textbox"}]
```

This creates a textbox that is data-bound to the `some-atom` atom and that has
the `fancy-textbox` class.

For documentation on specific components, you can look at the documentation of
the docstrings on the component functions.

Development
-----------

### Setup

Install node dependencies

```sh
npm install
```

### Running tests

```sh
# once
lein test once

# auto / watched
lein test auto
```
