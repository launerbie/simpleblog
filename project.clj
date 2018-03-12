(defproject simpleblog "0.1.0-SNAPSHOT"
  :description "A simple blog"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [compojure "1.1.8"]
                 ]
  :main ^:skip-aot simpleblog.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
