(ns tie.test-helpers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [>! chan]]
            [goog.dom :as dom]))

(defn render< [component]
  (let [res (chan)
        node (dom/createDom "div")]
    (r/render component node
      #(go (>! res (dom/getFirstElementChild node))))
    res))

(defn wait< []
  (let [c (chan)]
    (js/setTimeout
      #(go (>! c :done)) 1)
    c))
