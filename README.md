tie
===

[![Build Status](https://travis-ci.org/dotboris/tie.svg?branch=master)](https://travis-ci.org/dotboris/tie)

A ClojureScript library that provides data-bound form controls for reagent.

Why
---

Building forms in reagent is pretty tedious. For every control, you need to
define `:value` and `:on-change`. These are always implemented the same way.
They read from an atom and write back to it. Tie solves this by letting you
just pass in the atom and the data binding is automatic.

Usage
-----

FIXME

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
