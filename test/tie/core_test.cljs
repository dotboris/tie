(ns tie.core-test
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [dommy.core :refer [sel1]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures async]]
            [cljs.core.async :refer [<!]]
            [tie.core :as t]
            [tie.test-helpers :as h]
            [reagent.core :as r]))

(def numbers
  [["1" "one"]
   ["2" "two"]
   ["3" "three"]
   ["4" "four"]
   ["5" "five"]])

(defn form [state]
  [:form
    [:label "First Name"
      [t/text {:atom (r/cursor state [:first-name])
               :id "first-name"}]]
    [:label "Last Name"
      [t/text {:atom (r/cursor state [:last-name])
               :id "last-name"}]]

    [:label "Favorite number"
      [t/select {:atom (r/cursor state [:fav-number])
                 :options numbers
                 :allow-nil true
                 :id "fav-number"}]]

    [:p "Do you think that there is things such as there as?"]
    (let [question-atom (r/cursor state [:thing-question])]
      [:label "Yes" [t/radio {:atom question-atom
                              :id "question-yes"
                              :value true}]]
      [:label "No" [t/radio {:atom question-atom
                             :id "question-no"
                             :value false}]])

    [:p "Check the colors that you like"]
    [:div
      [:label
        [t/checkbox {:atom (r/cursor state [:like-red])
                     :id "like-red"}]
        "Red"]
      [:label
        [t/checkbox {:atom (r/cursor state [:like-green])
                     :id "like-green"}]
        "Green"]
      [:label
        [t/checkbox {:atom (r/cursor state [:like-blue])
                     :id "like-blue"}]
        "Blue"]
      [:label
        [t/checkbox {:atom (r/cursor state [:like-yellow])
                     :id "like-yellow"}]
        "Yellow"]
      [:label
        [t/checkbox {:atom (r/cursor state [:like-pink])
                     :id "like-pink"}]
        "Pink"]]

    [:label "Please write a story"
      [t/textarea {:atom (r/cursor state [:story])
                   :id "story"}]]])

(deftest it-should-work-with-full-form
  (async done
    (go (let [state (r/atom {})
              form-el (<! (h/render< [form state]))]
          (h/change! (sel1 form-el :#first-name) "Bob")
          (h/change! (sel1 form-el :#last-name) "Dole")
          (h/select! (sel1 form-el :#fav-number) "3")
          (h/click! (sel1 form-el :#question-no))
          (h/click! (sel1 form-el :#like-blue))
          (h/click! (sel1 form-el :#like-pink))
          (h/change! (sel1 form-el :#story) "Mary had a little lamb...")
          (<! (h/wait<))
          (is (= "Bob" (:first-name @state)))
          (is (= "Dole" (:last-name @state)))
          (is (= "3" (:fav-number @state)))
          (is (false? (:thing-question @state)))
          (is (not (:like-red @state)))
          (is (not (:like-green @state)))
          (is (:like-blue @state))
          (is (not (:like-yellow @state)))
          (is (:like-pink @state))
          (is (= "Mary had a little lamb..." (:story @state))))
        (done))))
