(ns tie.text-test
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
    (go (let [text [t/text {:atom state}]
              el (<! (h/render< text))]
          (is el)
          (is (= "INPUT" (.-tagName el)))
          (is (= "text" (.-type el))))
        (done))))

(deftest text-should-put-nil-value-as-empty-string
  (async done
    (go (let [text [t/text {:atom state}]
              el (<! (h/render< text))
              val (.-value el)]
          (is (= "" val)))
        (done))))

(deftest text-should-get-value-from-atom
  (async done
    (go (reset! state "foobar")
        (let [text [t/text {:atom state}]
              el (<! (h/render< text))
              val (.-value el)]
          (is (= "foobar" val)))
        (done))))

(deftest text-should-update-from-atom
  (async done
    (go (let [text [t/text {:atom state}]
              el (<! (h/render< text))]
          (is (= "" (.-value el)))
          (reset! state "stuff")
          (<! (h/wait<))
          (is (= "stuff" (.-value el))))
        (done))))

(deftest text-should-set-atom-from-value
  (async done
    (go (let [text [t/text {:atom state}]
              el (<! (h/render< text))]
          (h/change! el "things")
          (<! (h/wait<))
          (is (= "things" @state)))
        (done))))

(deftest text-should-set-empty-values-as-nil
  (async done
    (go (reset! state "stuff")
        (let [text [t/text {:atom state}]
              el (<! (h/render< text))]
          (h/change! el "")
          (<! (h/wait<))
          (is (nil? @state)))
        (done))))

(deftest it-should-assign-extra-options
  (async done
    (go (let [text [t/text {:atom state :data-foo "bar"}]
              el (<! (h/render< text))]
          (is (= "bar" (.. el -dataset -foo))))
        (done))))

(deftest it-should-not-assign-atom-options
  (async done
    (go (let [text [t/text {:atom state}]
              el (<! (h/render< text))]
          (is (nil? (aget el "atom"))))
        (done))))
