(ns contrast.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [contrast.events :as events]
            [contrast.views :as views]))

; render `views/root` component into `#app` div in `public/index.html`
(defn- mount-root []
  (r/render [views/root] (.getElementById js/document "app")))

; `dispatch-sync` to ensure everything in `:init` event happens before first render
; then `mount-root`
(defn init! []
  (rf/dispatch-sync [::events/init])
  (mount-root))
