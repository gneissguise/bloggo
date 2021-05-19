(ns bloggo.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [bloggo.db :as db]
            [bloggo.pages :as pages]))

(defroutes app-routes
  (GET "/" [] (pages/index (db/list-articles)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
