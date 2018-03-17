(defproject simpleblog "0.1.0-SNAPSHOT"
  :description "A simple blog"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 ]
  :main ^:skip-aot simpleblog.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
