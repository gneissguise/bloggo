(ns bloggo.pages
  (:require [hiccup.page :refer [html5]]))

(defn base-page [& body]
  (html5
   [:head [:title "Bloggo!"]]
   [:body
    [:h1 "Bloggo!"]
    body]))

(defn index [articles]
  (base-page
   (for [a articles]
     [:h2 [:a {:href (str "/article/" (:_id a))} (:title a)]])))

(defn article [a]
  (base-page
   [:small (:created a)]
   [:h1 (:title a)]
   [:p (:body a)]))
