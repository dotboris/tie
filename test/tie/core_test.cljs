(ns tie.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [tie.core]))

(deftest a-test
  (testing "math"
    (is (= 4 (+ 2 2)))))
