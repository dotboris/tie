(ns tie.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [tie.text-test]
            [tie.checkbox-test]))

(doo-tests 'tie.text-test
           'tie.checkbox-test)
