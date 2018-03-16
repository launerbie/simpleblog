(ns simpleblog.core
  (:require [org.httpkit.server :as s]
            [compojure.core :refer :all])
  )


(defroutes handler
  (GET "/" [] {:status 200
               :headers {"Content-Type" "text/plain"}
               :body    "<h1> My simple blog page </h1>"})
  (GET "/posts" [] "Show index of blog posts here.")
  (GET "/posts/:id" [id] (str "Blog entry number: " (str id)))
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

