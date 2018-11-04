(ns ^:figwheel-no-load contrast.dev
  (:require [contrast.core :as core]
            [devtools.core :as devtools]
            [re-frisk.core :as re-frisk]))

(enable-console-print!)

(devtools/install!)

(re-frisk/enable-re-frisk!)

(core/init!)
