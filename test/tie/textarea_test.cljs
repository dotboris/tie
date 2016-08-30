(ns tie.textarea-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures async]]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r]
            [tie.test-helpers :as h]
            [tie.core :as t]))

(def state (r/atom nil))
(def reset-state {:before #(reset! state nil)})
(use-fixtures :each reset-state)

(deftest it-should-render
  (async done
    (go (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))]
          (is el))
        (done))))

(deftest it-should-put-nil-value-as-empty-string
  (async done
    (go (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))
              val (.-value el)]
          (is (= "" val)))
        (done))))

(deftest it-should-get-value-from-atom
  (async done
    (go (reset! state "foobar")
        (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))
              val (.-value el)]
          (is (= "foobar" val)))
        (done))))

(deftest it-should-update-from-atom
  (async done
    (go (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))]
          (is (= "" (.-value el)))
          (reset! state "stuff")
          (<! (h/wait<))
          (is (= "stuff" (.-value el))))
        (done))))

(deftest it-should-set-atom-from-value
  (async done
    (go (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))]
          (h/change! el "things")
          (<! (h/wait<))
          (is (= "things" @state)))
        (done))))

(deftest it-should-set-empty-values-as-nil
  (async done
    (go (reset! state "stuff")
        (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))]
          (h/change! el "")
          (<! (h/wait<))
          (is (nil? @state)))
        (done))))

(deftest it-should-assign-extra-options
  (async done
    (go (let [area [t/textarea {:atom state :data-foo "bar"}]
              el (<! (h/render< area))]
          (is (= "bar" (.. el -dataset -foo))))
        (done))))

(deftest it-should-not-assign-atom-options
  (async done
    (go (let [area [t/textarea {:atom state}]
              el (<! (h/render< area))]
          (is (nil? (aget el "atom"))))
        (done))))
