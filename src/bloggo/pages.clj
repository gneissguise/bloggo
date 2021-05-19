(ns bloggo.pages
  (:require [hiccup.page :refer [html5]]))

(defn index [articles]
  (html5 [:h1 "Bloggo!"]
         (for [a articles]
           [:h2 (:title a)])))
