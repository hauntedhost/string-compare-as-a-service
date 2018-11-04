(ns contrast.subs
  (:require [re-frame.core :as rf]))

; simple subscription that returns :a key from db
; see: https://github.com/Day8/re-frame/blob/master/docs/CodeWalkthrough.md#reg-sub
(rf/reg-sub
 ::a
 (fn [db] (get db :a)))

(rf/reg-sub
 ::b
 (fn [db] (get db :b)))

(rf/reg-sub
 ::comparing?
 (fn [db] (get db :comparing?)))

(rf/reg-sub
 ::compare-response
 (fn [db] (get db :compare-response)))

; notice that second funny-looking arg
; this is a chained subscription
; this compare-message sub will only be called when compare-response changes
; see: https://github.com/Day8/re-frame/blob/master/examples/todomvc/src/todomvc/subs.cljs#L126
(rf/reg-sub
 ::compare-message
 :<- [::compare-response]
 (fn [response]
   (case (:areEqual response)
     true "the strings match"
     false "the strings do not match"
     nil)))

(rf/reg-sub
 ::error-message
 (fn [db] (get db :compare-error)))
