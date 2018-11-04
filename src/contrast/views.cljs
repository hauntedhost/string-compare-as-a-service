(ns contrast.views
  (:require
   [re-frame.core :as rf]
   [contrast.events :as events]
   [contrast.subs :as subs]))

; helper to pull target value from js event
(defn- get-target-value [e]
  (-> e .-target .-value))

; returns component with compare message or nil
(defn- compare-message []
  (let [message @(rf/subscribe [::subs/compare-message])]
    (js/console.log "rendering [compare-message]")
    (if-not (nil? message)
      [:div.success message])))

; returns component with error message or nil
(defn- error-message []
  (let [message @(rf/subscribe [::subs/error-message])]
    (js/console.log "rendering [error-message]")
    (if-not (nil? message)
      [:div.error message])))

; this form component has three subscriptions
; it will only re-render if values for these subscriptions change
; see: https://github.com/Day8/re-frame/blob/master/docs/CodeWalkthrough.md#subscribing
(defn- form []
  (let [a @(rf/subscribe [::subs/a])
        b @(rf/subscribe [::subs/b])
        comparing? @(rf/subscribe [::subs/comparing?])]
    (js/console.log "rendering [form]")
    [:div
     [:input {:type "text"
              :placeholder "enter a"
              :disabled comparing?
              :value a
              :on-change #(rf/dispatch [::events/change-a (get-target-value %)])}]
     [:input {:type "text"
              :placeholder "enter b"
              :disabled comparing?
              :value b
              :on-change #(rf/dispatch [::events/change-b (get-target-value %)])}]
     [:button {:disabled comparing?
               :on-click #(rf/dispatch [::events/compare])} "compare"]
     [compare-message]
     [error-message]]))

; public root component rendered from core/mount-root
; now it's turtles all the way down
(defn root []
  (js/console.log "rendering [root]")
  [:div
   [:h3 "string compare as a service"]
   [form]])
