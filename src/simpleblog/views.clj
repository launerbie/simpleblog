(ns simpleblog.views
  (:require [hiccup.page :as h]
            [hiccup.form :as form]
            [hiccup.core :refer [html]]
            [ring.util.anti-forgery :as anti-forgery]))

(defn base
  [title & body]
  (h/html5
    [:head
     [:title title]]
     [:body body]
    ))

(defn root []
  (base "Simpleblog - Home" [:h1 "My simple blog page"]))

(defn blogpost-new-form []
[:div {:id "blogpost-new-form"}
 (form/form-to [:post "/newpost"]
                 (anti-forgery/anti-forgery-field)
                 (form/label "title" "Title:")
                 (form/text-field "title")
                 (form/label "body" "Create new blogpost")
                 (form/text-area "body")
                 (form/submit-button "Submit"))])

(defn blogpost-new []
  (base "Simpleblog - New Post" (blogpost-new-form)))


(defn blogpost-index [posts]
  (base "Simpleblog - Index"
        (for [post posts] (html
          [:h3 (:title post)]
          [:p (:text post)]))
  ))

(defn four-o-four []
  (base "Simpleblog - Page not found" [:h1 "Page not found"]))

