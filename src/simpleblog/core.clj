(ns simpleblog.core
  (:require [org.httpkit.server :as s]
            [clojure.java.jdbc :as jdbc]
            [compojure.core :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer :all])
  )

(defn db-spec []
  {:dbtype "postgresql"
   :dbname "simpleblog"
   :hostname "localhost"
   :user "simpleblog"
   :password "secretpassword"}) ;To do: load user/pass from file

(def posts-table-ddl
  (jdbc/create-table-ddl
    :posts
    [[:id :serial "PRIMARY KEY"]
     [:title "varchar(100)" ]]))

(def postdetail-table-ddl
  (jdbc/create-table-ddl
    :postdetail
    [[:id :serial :primary :key]
     [:text "text"]
     [:posts :int "REFERENCES posts"]]))

(def comments-table-ddl
  (jdbc/create-table-ddl
    :comments
    [[:id :serial :primary :key]
     [:name "varchar(100)"]
     [:comment "text"]
     [:posts :int "REFERENCES posts"]]))

(defn insert-blogpost
  [title body]
  (let [result (jdbc/insert! (db-spec) :posts {:title title})
        postid ((first result) :id)]
     (jdbc/insert! (db-spec) :postdetail {:text body :posts (int postid)})))

(defn get-post
  [postid]
  (jdbc/query (db-spec) ["SELECT title FROM posts WHERE id = ?" postid]))

(defn post-blogpost
  [req]
  (let [title (get (:params req) :title)
        body  (get (:params req) :body)]
  (println req)
  (insert-blogpost title body)
  (str "Blog post has been submitted.")
  ;(redirect "/posts")
  ))

(defroutes handler
  (GET "/" [] {:status 200
               :headers {"Content-Type" "text/plain"}
               :body    "<h1> My simple blog page </h1>"})
  (GET "/posts" [] "Show index of blog posts here.")
  (POST "/posts" req (post-blogpost req))
  (GET "/posts/:id" [id] (str "Blog entry number: " (str id)))
  (GET "/test/:name" [name] (str "Hello " name))
  (GET "/test" [] "test"))

(defn create-server [port]
  (s/run-server (wrap-defaults handler api-defaults) {:port port}))

(defn stop-server [server]
  (server :timeout 100))

(defn -main
  [port & args]
  (create-server (Integer/parseInt port))
  (println "Server listening on port: " port))

