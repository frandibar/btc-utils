(ns bitcoin.unit
  (:refer-clojure :exclude [+ - = > >= < <=])
  )

(def ^:const SAT 100000000)
(def ^:const SAT_DEC 8)

(defprotocol BTCUnits
  (to-btc [this])
  (to-bit [this])
  (to-sat [this])
  )

(defrecord Btc [amount]
  BTCUnits
  (to-btc [this]
    (with-precision SAT_DEC (/ (:amount this) SAT)))

  (to-bit [this]
    (with-precision SAT_DEC (/ (:amount this) 100)))

  (to-sat [this]
   (:amount this))
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn btc
  "Constructor for Btc, in btc units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC (* SAT amount)))))

(defn bit
  "Constructor for Btc, in bits units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC (* 100 amount)))))

(defn sat
  "Constructor for Btc, in satoshi units."
  [amount]
  {:pre [(number? amount)]}
  (Btc. (bigint (with-precision SAT_DEC amount))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Implement operators

(defmacro btc-operator
  [new-op overriden-op result-op]
  (let [h1 (gensym)
        h2 (gensym)
        h3 (gensym)
        t1 (gensym)
        t2 (gensym)
        t3 (gensym)]
    `(list
      (defmulti ~new-op (fn [~h1 & ~t1] (type ~h1)))
      (defmethod ~new-op Btc
        [~h2 & ~t2]
        (~result-op (apply ~overriden-op (map :amount (cons ~h2 ~t2)))))
      (defmethod ~new-op :default
        [~h3 & ~t3]
        (apply ~overriden-op (cons ~h3 ~t3)))
      ))
  )

(btc-operator + clojure.core/+ sat)
(btc-operator - clojure.core/- sat)
(btc-operator = clojure.core/= identity)
(btc-operator > clojure.core/> identity)
(btc-operator >= clojure.core/>= identity)
(btc-operator < clojure.core/< identity)
(btc-operator <= clojure.core/<= identity)

;; i.e. This macro expands as follows for +:
;; (defmulti + (fn [head & tail] (type head)))
;; (defmethod + Btc
;;   [head & tail]
;;   (sat (apply clojure.core/+ (map :amount (cons head tail)))))
;; (defmethod + :default
;;   [head & tail]
;;   (apply clojure.core/+ (cons head tail)))


;; Sample usage:
;; (use 'bitcoin.unit)
;; (to-sat (btc 2))
;= 200000000
;; (= (btc 1) (sat 10000000))
;= true
