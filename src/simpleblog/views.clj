(ns simpleblog.views
  (:require [hiccup.page :as h]
            [hiccup.form :as f]
            [hiccup.core :refer [html]]))

(defn base
  [title & body]
  (h/html5
    [:head
     [:title title]]
     [:body body]
    ))

(defn root []
  (base "Simpleblog - Home" [:h1 "My simple blog page"]))

(defn blogpost-new []
  (base "Simpleblog - New Post" [:h1 "Submit a new blog post"]))

(defn blogpost-index [posts]
  (base "Simpleblog - Index"
        (for [post posts] (html
          [:h3 (:title post)]
          [:p (:text post)]))
  ))

(defn four-o-four []
  (base "Simpleblog - Page not found" [:h1 "Page not found"]))

