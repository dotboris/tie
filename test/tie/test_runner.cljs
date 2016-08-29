(ns tie.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [tie.text-test]))

(doo-tests 'tie.text-test)
