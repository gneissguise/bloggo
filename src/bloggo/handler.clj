(ns bloggo.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [ring.middleware.session :as session]
            [bloggo.db :as db]
            [bloggo.pages :as pages]
            [bloggo.admin :as admin]))

(defroutes app-routes
  (GET "/" [] (pages/index (db/list-articles)))
  (GET "/article/:artid" [artid] (pages/article
                                   (db/get-article-by-id artid)))
  (GET "/admin/login" [:as {session :session}]
       (if (:admin session)
         (resp/redirect "/")
         (pages/admin-login)))
  (POST "/admin/login" [login password]
        (if (admin/check-login login password)
          (-> (resp/redirect "/")
              (assoc-in [:session :admin] true))
          (pages/admin-login "Invalid username or password")))
  (GET "/admin/logout" []
       (-> (resp/redirect "/")
           (assoc-in [:session :admin] false)))
  (route/not-found "Not Found"))

(defroutes admin-routes
  (GET "/article/new" [] (pages/edit-article nil))
  (POST "/article" [title body]
        (do (db/create-article title body)
            (resp/redirect "/")))
  (GET "/article/:artid/edit" [artid] (pages/edit-article
                                       (db/get-article-by-id artid)))
  (DELETE "/article/:artid" [artid]
          (do (db/delete-article artid)
              (resp/redirect "/")))
  (POST "/article/:artid" [artid title body]
        (do (db/update-article artid title body)
            (resp/redirect (str "/article/" artid)))))

(defn wrap-admin-only [handler]
  (fn [request]
    (if (-> request :session :admin)
      (handler request)
      (resp/redirect "/admin/login"))))

(def app
  (-> (routes (wrap-routes admin-routes wrap-admin-only)
              app-routes)
      (wrap-defaults site-defaults)
      session/wrap-session))
