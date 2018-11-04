(ns contrast.events
  (:require-macros [adzerk.env :as env])
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [contrast.db :as db]))

; this pulls in url from env var or raises
(env/def API_BASE_URL :required)

; init is simple in this case, just return the default-db
; see: https://github.com/Day8/re-frame/blob/master/docs/CodeWalkthrough.md#initialize
(rf/reg-event-db
 ::init
 (fn [_ _] db/default-db))

; simple event updates `:a` key in db with new-text
(rf/reg-event-db
 ::change-a
 (fn [db [_ new-text]]
   (assoc db :a new-text)))

; update `:b` key in db with new-text
(rf/reg-event-db
 ::change-b
 (fn [db [_ new-text]]
   (assoc db :b new-text)))

; our first `reg-event-fx`
; see: https://github.com/Day8/re-frame/blob/master/docs/EffectfulHandlers.md#another-example
;
; instead of receiving and returning a db directly,
; this handler receives and returns a map of "coeffects"
; one key of which can be `:db`
; (if `:db` key is not included in return map, db is simply not changed)
;
; in this case we return a map with `:http-xhrio` key describing an ajax call
; we want to make, and the events we want to trigger on success and failure
; this particular effect is possible through this library: https://github.com/Day8/re-frame-http-fx
;
; we also include the `:db` key in our return map and update `:comparing?` to true
; our ui can use this key to e.g. disable inputs and submit button while waiting for
; server response
(rf/reg-event-fx
 ::compare
 (fn [{:keys [db]}]
   (let [a (:a db)
         b (:b db)]
     {:http-xhrio {:method          :get
                   :uri             (str API_BASE_URL "/compare")
                   :params          {:a a :b b}
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [::compare-success]
                   :on-failure      [::compare-failure]}
      :db (assoc db :comparing? true)})))

; event called when ajax response from compare event is successful
; this could just as eassily be another `reg-event-fx` but in this case we just
; want to return an updated db
(rf/reg-event-db
 ::compare-success
 (fn [db [_ response]]
   (merge db {:comparing? false
              :compare-error nil
              :compare-response response})))

; event called when ajax response from compare event fails
; returns an updated db
(rf/reg-event-db
 ::compare-failure
 (fn [db [_ error]]
   (merge db {:comparing? false
              :compare-error (get-in error [:response :error] "unknown error")
              :compare-response (get error :response)})))
