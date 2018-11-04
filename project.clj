(defproject contrast "0.1.0-SNAPSHOT"
  :description "contrast app"

  :dependencies [[adzerk/env "0.4.0"]
                 [cljs-ajax "0.7.3"]
                 [com.cognitect/transit-clj "0.8.313"]
                 [day8.re-frame/http-fx "0.1.4"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [re-frame "0.10.5"]
                 [re-frisk "0.5.3"]
                 [reagent "0.8.1"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.16"]]

  :min-lein-version "2.5.0"

  :jvm-opts ^:replace ["--add-modules"
                       "java.xml.bind"
                       "-server"
                       "-Xmx3g"
                       "-Xverify:none"]

  :clean-targets ^{:protect false}

  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["public"]

  :figwheel {:http-server-root "."
             :nrepl-port 7002
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
             :css-dirs ["public/css"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "env/dev/cljs"]
                        :compiler
                        {:main "contrast.dev"
                         :output-to "public/js/app.js"
                         :output-dir "public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :pretty-print  true}
                        :figwheel
                        {:on-jsload "contrast.core/mount-root"
                         :open-urls ["http://localhost:3449/index.html"]}}
                       :release
                       {:source-paths ["src" "env/prod/cljs"]
                        :compiler
                        {:output-to "public/js/app.js"
                         :output-dir "public/js/release"
                         :asset-path   "js/out"
                         :optimizations :advanced
                         :pretty-print false}}}}

  :aliases {"package" ["do" "clean" ["cljsbuild" "once" "release"]]}

  :profiles {:dev {:source-paths ["src" "cljs_src" "env/dev/clj"]
                   :dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.16"]
                                  [nrepl "0.4.4"]
                                  [cider/piggieback "0.3.8"]]}})
