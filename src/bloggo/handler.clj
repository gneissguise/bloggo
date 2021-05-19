(ns bloggo.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [bloggo.db :as db]
            [bloggo.pages :as pages]))

(defroutes app-routes
  (GET "/" [] (pages/index (db/list-articles)))
  (GET "/article/new" [] (pages/edit-article nil))
  (POST "/article" [title body]
        (do (db/create-article title body)
            (resp/redirect "/")))
  (GET "/article/:artid" [artid] (pages/article
                                   (db/get-article-by-id artid)))
  (GET "/article/:artid/edit" [artid] (pages/edit-article
                                       (db/get-article-by-id artid)))
  (POST "/article/:artid" [artid title body]
        (do (db/update-article artid title body)
            (resp/redirect (str "/article/" artid))))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
