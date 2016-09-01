(ns tie.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [tie.text-test]
            [tie.textarea-test]
            [tie.checkbox-test]
            [tie.radio-test]
            [tie.select-test]))

(doo-tests 'tie.text-test
           'tie.textarea-test
           'tie.checkbox-test
           'tie.radio-test
           'tie.select-test)
