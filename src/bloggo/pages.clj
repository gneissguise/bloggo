(ns bloggo.pages
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn base-page [& body]
  (html5
   [:head [:title "Bloggo!"]]
   [:body
    [:h1 [:a {:href "/"} "Bloggo!"]]
    [:a {:href "/article/new"} "New Article"]
    body]))

(defn index [articles]
  (base-page
   (for [a articles]
     [:h2 [:a {:href (str "/article/" (:_id a))} (:title a)]])))

(defn article [a]
  (base-page
   [:a {:href (str "/article/" (:_id a) "/edit")} "Edit Article"]
   [:small (:created a)]
   [:h1 (:title a)]
   [:p (:body a)]))

(defn edit-article [a]
  (base-page
   (form/form-to
    [:post (if a
             (str "/article/" (:_id a))
             "/article")]
    (form/label "title" "Title")
    (form/text-field "title" (:title a))
    (form/label "body" "Body")
    (form/text-area "body" (:body a))
    (anti-forgery-field)
    (form/submit-button "Save"))))
