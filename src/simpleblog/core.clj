(ns simpleblog.core
  (:require [org.httpkit.server :as s]
            [compojure.core :refer :all])
  )

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1> My simple blog page </h1>"})

(defn create-server [port]
  (s/run-server handler {:port port}))

(defn stop-server [server]
  (server :timeout 100))

(defn -main
  [port & args]
  (create-server (Integer/parseInt port))
  (println "Server listening on port: " port))

