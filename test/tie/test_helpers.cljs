(ns tie.test-helpers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [>! chan]]
            [cljs-react-test.utils :as rtu]
            [cljs-react-test.simulate :as sim]
            [goog.dom :as dom]
            [goog.events :as events]))

(defn render< [component]
  (let [res (chan)
        node (rtu/new-container!)]
    (r/render component node
      #(go (>! res (dom/getFirstElementChild node))))
    res))

(defn wait< []
  (let [c (chan)]
    (js/setTimeout
      #(go (>! c :done)) 1)
    c))

(defn change!
  "Change the value of a control and fire the on-change event for that input."
  [el val]
  (set! (.-value el) val)
  (sim/change el val))

(defn click! [el]
  (let [e (.createEvent js/document "MouseEvents")]
    ; fun fact, js events are stupid...
    (.initMouseEvent e
      "click" true true js/window 0 0 0 0 0 false false false false 0 nil)
    (.dispatchEvent el e)))

(defn print-el [el]
  (let [wrapper (rtu/new-container!)]
    (.appendChild wrapper el)
    (println (.-innerHTML wrapper))))
