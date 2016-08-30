(ns tie.radio-test
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
    (go (let [radio [t/radio {:atom state}]
              el (<! (h/render< radio))]
          (is el)
          (is (= "INPUT" (.-tagName el)))
          (is (= "radio" (.-type el))))
        (done))))

(deftest it-should-allow-extra-props
  (async done
    (go (let [radio (<! (h/render< [t/radio {:atom state :data-butt "stuff"}]))]
          (is (= "stuff" (.. radio -dataset -butt))))
        (done))))

(deftest it-should-not-set-atom-prop
  (async done
    (go (let [radio (<! (h/render< [t/radio {:atom state}]))]
          (is (not (.-atom radio))))
        (done))))

(deftest it-should-select-the-radio-with-matching-value
  (async done
    (go (reset! state "fi")
        (let [fi-radio [t/radio {:atom state :value "fi"}]
              fi-el (<! (h/render< fi-radio))
              fo-radio [t/radio {:atom state :value "fo"}]
              fo-el (<! (h/render< fo-radio))
              fum-radio [t/radio {:atom state :value "fum"}]
              fum-el (<! (h/render< fum-radio))]
          (is (.-checked fi-el))
          (is (not (.-checked fo-el)))
          (is (not (.-checked fum-el))))
        (done))))

(deftest it-should-select-nothing-with-no-matching-value
  (async done
    (go (reset! state "butt")
        (let [fi-radio [t/radio {:atom state :value "fi"}]
              fi-el (<! (h/render< fi-radio))
              fo-radio [t/radio {:atom state :value "fo"}]
              fo-el (<! (h/render< fo-radio))
              fum-radio [t/radio {:atom state :value "fum"}]
              fum-el (<! (h/render< fum-radio))]
          (is (not (.-checked fi-el)))
          (is (not (.-checked fo-el)))
          (is (not (.-checked fum-el))))
        (done))))

(deftest it-should-change-selected-on-update
  (async done
    (go (reset! state "a")
        (let [a-radio [t/radio {:atom state :value "a"}]
              a-el (<! (h/render< a-radio))
              b-radio [t/radio {:atom state :value "b"}]
              b-el (<! (h/render< b-radio))]
          (is (and (.-checked a-el)
                   (not (.-checked b-el))))
          (reset! state "b")
          (<! (h/wait<))
          (is (and (not (.-checked a-el))
                   (.-checked b-el))))
        (done))))

(deftest it-should-select-from-non-strings
  (async done
    (go (reset! state 1)
        (let [one (<! (h/render< [t/radio {:atom state :value 1}]))
              two (<! (h/render< [t/radio {:atom state :value 2}]))]
          (is (.-checked one))
          (is (not (.-checked two))))
        (done))))

(deftest it-should-assing-value-from-selected
  (async done
    (go (let [yep (<! (h/render< [t/radio {:atom state :value true}]))
              nope (<! (h/render< [t/radio {:atom state :value false}]))]
          (h/click! yep)
          (<! (h/wait<))
          (is (true? @state))
          (h/click! nope)
          (<! (h/wait<))
          (is (false? @state)))
        (done))))
