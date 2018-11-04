(ns ^:figwheel-no-load contrast.dev
  (:require
    [contrast.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
