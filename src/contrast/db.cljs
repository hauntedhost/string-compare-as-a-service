(ns contrast.db)

; initial db map for entire app state
(def default-db
  {:a "foo"
   :b "bar"
   :comparing? false
   :compare-error nil
   :compare-response nil})
