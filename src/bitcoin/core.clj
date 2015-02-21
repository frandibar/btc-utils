(ns bitcoin.core)

(def ^:const SAT 100000000)
(def ^:const SAT_DEC 8)


(defprotocol BTCAmount
  (to-sat [this])
  (to-btc [this])
  )

(declare btc)

;; These functions aren't defined in the protocol because functions
;; declared in protocols don't support variadic args.

(defn btc-
  [head & tail]
  (btc (- (:amount head) (apply + (map :amount tail)))
       'sat))

(defn btc=
  [head & tail]
  (apply = (conj (map :amount tail)
                 (:amount head))))

(defrecord Btc [amount]
  BTCAmount
  (to-sat [this]
   (:amount this))

  (to-btc [this]
    (with-precision SAT_DEC (/ (:amount this) SAT)))
  )

(defn btc
  "Constructor for Btc."
  ([amount]
   (btc amount 'btc))

  ([amount unit]
   {:pre [(number? amount)
          (get #{'sat 'btc} unit)]}
   (cond
     (= 'btc unit)
     (Btc. (bigint (with-precision SAT_DEC (* SAT amount))))

     (= 'sat unit)
     (Btc. (bigint (with-precision SAT_DEC amount))))))

(defmulti + (fn [head & tail] (type head)))
(defmethod + bitcoin.core.Btc
  [head & tail]
  (btc (apply clojure.core/+ (conj (map :amount tail)
                                   (:amount head)))
       'sat))

;; TODO
;; Why not have a (btc n), (sat n), (bits n) ?

;; Sample usage:
;; (use 'bitcoin.core)
;; (to-sat (->Btc 2 'btc))
;= 200000000
;; (btc-eq (btc 1) (btc 10000000 'sat))
;= true
