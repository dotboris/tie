(ns tie.select-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures async]]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r]
            [tie.test-helpers :as h]
            [tie.core :as t]))

(defn selected-value [select]
  (let [index (.-selectedIndex select)
        options (h/children select)
        option (nth options index)]
    (.-value option)))

(def state (r/atom nil))
(def reset-state {:before #(reset! state nil)})
(use-fixtures :each reset-state)

(deftest it-should-render
  (async done
    (go (let [options [["1" "one"] ["2" "two"] ["3" "three"]]
              el (<! (h/render< [t/select {:atom state :options options}]))
              option-els (h/children el)]
          (testing "select renders"
            (is el)
            (is (= "SELECT" (.-tagName el))))
          (testing "optons render"
            (is (= 3 (count option-els)))
            (let [o (nth option-els 0)]
              (is (= "1" (.-value o)))
              (is (= "one" (.-text o))))
            (let [o (nth option-els 1)]
              (is (= "2" (.-value o)))
              (is (= "two" (.-text o))))
            (let [o (nth option-els 2)]
              (is (= "3" (.-value o)))
              (is (= "three" (.-text o))))))
        (done))))

(deftest it-should-render-empty-option-with-allow-nil
  (async done
    (go (let [options [["foo" "foo"]]
              el (<! (h/render< [t/select {:atom state
                                           :options options
                                           :allow-nil true}]))
              option-els (h/children el)]
          (is (= 2 (count option-els)))
          (is (some #(and (= "" (.-value %))
                          (= "" (.-text %)))
                    option-els)))
        (done))))

(deftest it-should-render-nil-text-if-present
  (async done
    (go (let [options [["foo" "foo"]]
              el (<! (h/render< [t/select {:atom state
                                           :options options
                                           :allow-nil true
                                           :nil-text "butts"}]))
              option-els (h/children el)]
          (is (= 2 (count option-els)))
          (is (some #(and (= "" (.-value %))
                          (= "butts" (.-text %)))
                    option-els)))
        (done))))

(deftest it-should-render-extra-options
  (async done
    (go (let [el (<! (h/render< [t/select {:atom state :data-foo "bar"}]))]
          (is (= "bar" (.. el -dataset -foo))))
        (done))))

(deftest it-should-not-set-special-options
  (async done
    (go (let [el (<! (h/render< [t/select {:atom state
                                           :options []
                                           :allow-nil true
                                           :nil-text "- nothing -"}]))]
          (is (not (.-atom el)))
          (is (not (.-allow_nil el)))
          (is (not (.-allow-nil el)))
          (is (not (.-nil_text el)))
          (is (not (.-nil-text el))))
        (done))))

(deftest it-should-select-from-atom
  (async done
    (go (reset! state "b")
        (let [options [["a" "A"] ["b" "B"] ["c" "C"]]
              el (<! (h/render< [t/select {:atom state :options options}]))]
          (is (= "b" (selected-value el))))
        (done))))

(deftest it-should-select-nil-from-atom
  (async done
    (go (let [options [["a" "A"] ["b" "B"] ["c" "C"]]
              el (<! (h/render< [t/select {:atom state
                                           :options options
                                           :allow-nil true}]))]
          (is (= "" (selected-value el))))
        (done))))

(deftest it-should-store-selected-to-atom
  (async done
    (go (let [options [["a" "A"] ["b" "B"]]
              el (<! (h/render< [t/select {:atom state :options options}]))]
          (is (nil? @state))
          (h/select! el "b")
          (<! (h/wait<))
          (is (= "b" @state)))
        (done))))

(deftest it-should-store-nil-when-selecting-empty-string
  (async done
    (go (reset! state "a")
        (let [el (<! (h/render< [t/select {:atom state
                                           :options [["a" "A"]]
                                           :allow-nil true}]))]
          (is (= "a" @state))
          (h/select! el "")
          (<! (h/wait<))
          (is (nil? @state)))
        (done))))
