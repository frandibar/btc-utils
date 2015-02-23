(ns bitcoin.transaction-test
  (:refer-clojure :exclude [+ - = > >= < <=])
  (:require [clojure.test :refer :all]
            [bitcoin.unit :refer :all]
            [bitcoin.transaction :refer :all]))

(deftest transaction-test

  (testing "test-fee"
    (is (= (calculate-fee 1 1)
           (btc 0.0001)))
    )
  )
