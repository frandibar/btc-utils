(ns bitcoin.transaction
  (:use [bitcoin.unit :only (btc)]))

(defn trx-size
  [ninputs noutputs]
  (+ 10 (* 148 ninputs) (* 34 noutputs)))

(defn calculate-fee
  [ninputs noutputs]
  (let [fee-per-kb 0.0001
        size (trx-size ninputs noutputs)
        kb (/ size 1000)
        kb-adjusted (if (> 0 (mod kb 1000))
                      (+ 1 kb)
                      kb)]
    (btc (max (* kb-adjusted fee-per-kb)
              fee-per-kb)))
  )
