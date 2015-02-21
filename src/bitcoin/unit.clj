(ns bitcoin.unit
  ;; (require '[clojure.core :as core])
  ;; (use '(clojure [core :only (+) :as core]))
  )

(def ^:const SAT 100000000)
(def ^:const SAT_DEC 8)

(def ^:const BIT 1000000)
(def ^:const BIT_DEC 6)

(defprotocol BTCAmount
  (to-sat [this])
  (to-btc [this])
  (to-bit [this])
  )

(declare sat)

;; These functions aren't defined in the protocol because functions
;; declared in protocols don't support variadic args.

;; (defn btc+
;;   [head & tail]
;;   (sat (- (:amount head) (apply + (map :amount tail)))))

(defn btc-
  [head & tail]
  (sat (- (:amount head) (apply + (map :amount tail)))))

(defn btc=
  [head & tail]
  (apply = (conj (map :amount tail)
                 (:amount head))))

(defrecord Btc [amount]
  BTCAmount
  (to-btc [this]
    (with-precision SAT_DEC (/ (:amount this) SAT)))

  (to-bit [this]
    (with-precision BIT_DEC (/ (:amount this) BIT)))

  (to-sat [this]
   (:amount this))
  )

(defn btc
  "Constructor for Btc."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC (* SAT amount)))))

(defn sat
  "Constructor for Btc."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC amount))))

(defn bit
  "Constructor for Btc."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision BIT_DEC (* BIT amount)))))

(defmulti btc+ (fn [head & tail] (type head)))
(defmethod btc+ Btc
  [head & tail]
  (sat (apply clojure.core/+ (conj (map :amount tail)
                                   (:amount head)))))

;; TODO
;; Why not have a (btc n), (sat n), (bits n) ?

;; Sample usage:
;; (use 'bitcoin.unit)
;; (to-sat (btc 2))
;= 200000000
;; (btc-eq (btc 1) (sat 10000000))
;= true
