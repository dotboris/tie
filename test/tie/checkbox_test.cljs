(ns tie.checkbox-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures async]]
            [cljs.core.async :refer [<!]]
            [cljs-react-test.simulate :as sim]
            [reagent.core :as r]
            [tie.test-helpers :as h]
            [tie.core :as t]))

(def state (r/atom nil))
(def reset-state {:before #(reset! state nil)})
(use-fixtures :each reset-state)

(deftest it-should-render
  (async done
    (go (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (is el)
          (is (= "INPUT" (.-tagName el)))
          (is (= "checkbox" (.-type el))))
        (done))))

(deftest it-should-not-be-checked-if-state-is-nil
  (async done
    (go (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (is (not (.-checked el))))
        (done))))

(deftest it-should-not-be-checked-if-state-is-false
  (async done
    (go (reset! state false)
        (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (is (not (.-checked el))))
        (done))))

(deftest it-should-be-checked-if-state-is-true
  (async done
    (go (reset! state true)
        (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (is (.-checked el)))
        (done))))

(deftest it-should-go-from-false-to-true
  (async done
    (go (reset! state false)
        (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (h/click! el)
          (<! (h/wait<))
          (is @state))
        (done))))

(deftest it-should-go-from-true-to-false
  (async done
    (go (reset! state true)
        (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (h/click! el)
          (<! (h/wait<))
          (is (not @state)))
        (done))))

(deftest it-should-assign-extra-options
  (async done
    (go (let [box [t/checkbox {:atom state :data-foo "bar"}]
              el (<! (h/render< box))]
          (is (= "bar" (.. el -dataset -foo))))
        (done))))

(deftest it-should-not-assign-atom-options
  (async done
    (go (let [box [t/checkbox {:atom state}]
              el (<! (h/render< box))]
          (is (nil? (aget el "atom"))))
        (done))))
