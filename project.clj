(defproject tie "0.1.0-SNAPSHOT"
  :description "A set of data-bound form controls for reagent"
  :url "https://github.com/dotboris/tie"
  :license {:name "MIT" :url "https://opensource.org/licenses/MIT"}
  :authors ["dotboris"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-doo "0.1.7"]]

  :source-paths ["src"]

  :cljsbuild
  {:builds {:test {:source-paths ["src" "test"]
                   :compiler {:output-to "target/tests/test.js"
                              :output-dir "target/tests/"
                              :main tie.test-runner
                              :optimizations :none}}}}

  :profiles
  {:test {:dependencies [[reagent "0.6.0-rc"]
                         [prismatic/dommy "1.1.0"]
                         [org.clojure/core.async "0.2.385"]]}}

  :doo {:build "test"
        :paths {:phantom "./node_modules/phantomjs-prebuilt/bin/phantomjs"}}

  :aliases {"test" ["with-profiles" "test" "doo" "phantom" "test"]})
