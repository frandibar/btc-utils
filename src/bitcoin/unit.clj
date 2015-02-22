(ns bitcoin.unit
  ;; (require '[clojure.core :as core])
  ;; (use '(clojure [core :only (+) :as core]))
  )

(def ^:const SAT 100000000)
(def ^:const SAT_DEC 8)

(defprotocol BTCAmount
  (to-sat [this])
  (to-btc [this])
  (to-bit [this])
  )

(defrecord Btc [amount]
  BTCAmount
  (to-btc [this]
    (with-precision SAT_DEC (/ (:amount this) SAT)))

  (to-bit [this]
    (with-precision SAT_DEC (/ (:amount this) 100)))

  (to-sat [this]
   (:amount this))
  )

(defn btc
  "Constructor for Btc, in btc units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC (* SAT amount)))))

(defn sat
  "Constructor for Btc, in satoshi units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC amount))))

(defn bit
  "Constructor for Btc, in bits units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC (* 100 amount)))))

(defmulti + (fn [head & tail] (type head)))
(defmethod + Btc
  [head & tail]
  (sat (apply clojure.core/+ (conj (map :amount tail)
                                   (:amount head)))))
(defmethod + :default
  [head & tail]
  (apply clojure.core/+ (conj tail head)))


(defmulti - (fn [head & tail] (type head)))
(defmethod - Btc
  [head & tail]
  (sat (clojure.core/- (:amount head)
                       (apply clojure.core/+ (map :amount tail)))))

(defmethod - :default
  [head & tail]
  (clojure.core/- head
                  (apply clojure.core/+ tail)))


(defmulti = (fn [head & tail] (type head)))
(defmethod = Btc
  [head & tail]
  (apply clojure.core/+ (conj (map :amount tail)
                              (:amount head))))

(defmethod = :default
  [head & tail]
  (apply clojure.core/= (conj (map :amount tail)
                              (:amount head))))

;; Sample usage:
;; (use 'bitcoin.unit)
;; (to-sat (btc 2))
;= 200000000
;; (btc-eq (btc 1) (sat 10000000))
;= true
