(ns tie.core-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures async]]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r]
            [tie.test-helpers :as h]
            [tie.core :as t]))

(enable-console-print!)

(def state (r/atom nil))

(def reset-state
  {:before #(reset! state nil)})

(use-fixtures :each reset-state)

(deftest text-should-render
  (async done
    (go
      (let [text [t/text {:atom state}]
            el (<! (h/render< text))]
        (is el)
        (done)))))
