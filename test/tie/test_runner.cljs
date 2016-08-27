(ns tie.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [tie.core-test]))

(doo-tests 'tie.core-test)
