(ns bloggo.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

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


(defn list-articles []
  (mc/find-maps db articles))