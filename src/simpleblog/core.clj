(ns simpleblog.core
  (:require [org.httpkit.server :as s]
            [clojure.java.jdbc :as jdbc]
            [compojure.core :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer :all]
            [simpleblog.views :as views])
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

(defn create-tables []
  (jdbc/db-do-commands (db-spec) posts-table-ddl)
  (jdbc/db-do-commands (db-spec) postdetail-table-ddl)
  (jdbc/db-do-commands (db-spec) comments-table-ddl))

(defn drop-tables []
  (jdbc/db-do-commands (db-spec) ["DROP TABLE comments"])
  (jdbc/db-do-commands (db-spec) ["DROP TABLE postdetail"])
  (jdbc/db-do-commands (db-spec) ["DROP TABLE posts"]))

(defn recreate-tables []
  (comp drop-tables create-tables))

(defn insert-blogpost
  [title body]
  (let [result (jdbc/insert! (db-spec) :posts {:title title})
        postid ((first result) :id)]
     (jdbc/insert! (db-spec) :postdetail {:text body :posts (int postid)})))

(defn get-post
  [postid]
  (jdbc/query (db-spec) ["SELECT title FROM posts WHERE id = ?" postid]))

(defn blogposts-all []
  (let [posts  (jdbc/query (db-spec)
                  ["SELECT title, text
                  FROM posts
                  INNER JOIN postdetail
                  ON posts.id = postdetail.posts"])]
  (println posts)
  (views/blogpost-index posts)))

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
  (GET  "/" [] (views/root))
  (GET  "/newpost" [] (views/blogpost-new))
  (POST "/newpost" req (post-blogpost req))
  (GET  "/posts" [] (blogposts-all))
  (GET  "/posts/:id" [id] (str "Blog entry number: " (str id))))

(defn create-server [port]
  (s/run-server (wrap-defaults handler site-defaults) {:port port}))

(defn stop-server [server]
  (server :timeout 100))

(defn -main
  [port & args]
  (create-server (Integer/parseInt port))
  (println "Server listening on port: " port))

