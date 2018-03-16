(ns simpleblog.core
  (:require [org.httpkit.server :as s]
            [compojure.core :refer :all])
  )


(defroutes handler
  (GET "/" [] {:status 200
               :headers {"Content-Type" "text/plain"}
               :body    "<h1> My simple blog page </h1>"})
  (GET "/posts" [] "<h1> My simple blog page </h1>")
  (GET "/test/:name" [name] (str "Hello " name))
  (GET "/test" [] "test"))

(defn create-server [port]
  (s/run-server handler {:port port}))

(defn stop-server [server]
  (server :timeout 100))

(defn -main
  [port & args]
  (create-server (Integer/parseInt port))
  (println "Server listening on port: " port))

