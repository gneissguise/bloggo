(ns bloggo.db
  (:require [monger.core :as mg]
            [monger.operators :refer [$set]]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(def db-connect-uri (or (System/getenv "CLJBLOGGO_MONGO_URI")
                        "mongodb://127.0.0.1/bloggo"))

(def articles "articles")

(def db (-> db-connect-uri
            mg/connect-via-uri
            :db))

(defn create-article [title body]
  (mc/insert db articles
             {:title title
              :body body
              :created (new java.util.Date)}))

(defn update-article [artid title body]
  (mc/update-by-id db articles (ObjectId. artid)
             {$set
              {:title title
              :body body}}))


(defn list-articles []
  (mc/find-maps db articles))

(defn get-article-by-id [artid]
  (mc/find-map-by-id db articles (ObjectId. artid)))
