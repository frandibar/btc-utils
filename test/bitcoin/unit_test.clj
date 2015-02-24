(ns bitcoin.unit-test
  (:refer-clojure :exclude [+ - = not= > >= < <=])
  (:require [clojure.test :refer :all]
            [bitcoin.unit :refer :all]))

(deftest unit-test

  (testing "test-conversion"
    (is (= (to-sat (btc 1.234))
           (to-sat (sat 123400000))))
  )

  (testing "test-constructors"
    (is (= (btc 1)
           (sat 100000000)
           (bit 1000000)))
    )

  (testing "test-eq"
    ;; self.assertEqual(amount.BitcoinAmount(10), amount.BitcoinAmount(10))
    (is (= (btc 10)
           (btc 10)))

    ;; self.assertEqual(amount.BitcoinAmount(10.1), amount.BitcoinAmount(10.1))
    (is (= (btc 10.1)
           (btc 10.1)))
    )

  (testing "test-add"
    ;; self.assertEqual(amount.BitcoinAmount(10) + amount.BitcoinAmount(0), amount.BitcoinAmount(10))
    (is (= (+ (btc 10)
                 (btc 0))
           (btc 10)))

    ;; self.assertEqual(amount.BitcoinAmount(10) + amount.BitcoinAmount(0.1), amount.BitcoinAmount(10.1))
    (is (= (+ (btc 10)
                 (btc 0.1))
           (btc 10.1)))

    ;; self.assertEqual(
    ;;     amount.BitcoinAmount(0.1) + amount.BitcoinAmount(0.1) + amount.BitcoinAmount(0.1),
    ;;     amount.BitcoinAmount(0.3)
    ;; )
    (is (= (+ (btc 0.1)
                 (btc 0.1)
                 (btc 0.1))
           (btc 0.3)))
    )

    ;; self.assertEqual(amount.BitcoinAmount(0.1) + amount.BitcoinAmount(0.1 + 0.1 + 0.1), amount.BitcoinAmount(0.4))
    (is (= (+ (btc 0.1)
                 (btc (+ 0.1 0.1 0.1)))
           (btc 0.4)))

    ;; self.assertEqual(
    ;;     amount.BitcoinAmount(0.000000001) + amount.BitcoinAmount(0.000000001),
    ;;     amount.BitcoinAmount(0)
    ;; )
    (is (= (+ (btc 0.000000001)
                 (btc 0.000000001))
           (btc 0)))

    ;; self.assertEqual((amount.BitcoinAmount(20999999.99999999) + amount.BitcoinAmount(0.00000001)).btc(), 21000000)
    (is (= (to-btc (+ (btc 20999999.99999999)
                         (btc 0.00000001)))
           21000000))

    ;; self.assertEqual(
    ;;     (amount.BitcoinAmount(20999999.99999999) + amount.BitcoinAmount(0.00000001)).satoshis(),
    ;;     2100000000000000
    ;; )
    (is (= (to-sat (+ (btc 20999999.99999999)
                         (btc 0.00000001)))
           2100000000000000))

    )
