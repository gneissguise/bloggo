(ns bloggo.pages
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn base-page [& body]
  (html5
   [:head [:title "Bloggo!"]
    [:link {:rel "stylesheet"
            :href "https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
            :integrity "sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
            :crossorigin "anonymous"}]]
   [:body
    [:div.container
     [:nav.navbar.navbar-expand-lg.navbar-light.bg-light
      [:a.navbar-brand {:href "/"} "Bloggo!"]
      [:ul.nav.justify-content-end
       [:li.nav-item
        [:a.nav-link {:href "/article/new"} "New Article"]]
       [:li.nav-item
        [:a.nav-link {:href "/admin/login"} "Login"]]
       [:li.nav-item
        [:a.nav-item.nav-link {:href "/admin/logout"} "Logout"]]]]
     body]]))

(def preview-len 270)

(defn- cut-body [body]
  (if (> (.length body) preview-len)
    (subs body 0 preview-len)
    body))

(defn index [articles]
  (base-page
   (for [a articles]
     [:article {:style "min-height:150px;"}
      [:h3 [:a {:href (str "/article/" (:_id a))} (:title a)]]
      [:p (-> a :body cut-body)]])))

(defn article [a]
  (base-page
   [:article {:style "min-height:400px;"}
    [:h2 (:title a)]
    [:small (:created a)]
    [:hr]
    [:p (:body a)]]
   (form/form-to
    [:delete (str "/article/" (:_id a) "/delete")]
    (anti-forgery-field)
    [:a.btn.btn-primary {:href (str "/article/" (:_id a) "/edit")} "Edit Article"]
    (form/submit-button {:class "btn btn-danger"} "Delete"))))

(defn edit-article [a]
  (base-page
   (form/form-to
    [:post (if a
             (str "/article/" (:_id a))
             "/article")]
    [:div.form-group
     (form/label "title" "Title")
     (form/text-field {:class "form-control"} "title" (:title a))]
    [:div.form-group(form/label "body" "Body")
     (form/text-area {:class "form-control" :rows "25"} "body" (:body a))]
    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Save"))))

(defn admin-login [& [msg]]
  (base-page
   (when msg
     [:div.alert.alert-danger msg])
   (form/form-to
    [:post "/admin/login"]
    [:div.form-group
     (form/label "login" "Login")
     (form/text-field {:class "form-control"} "login")]
    [:div.form-group
     (form/label "password" "Password")
     (form/password-field {:class "form-control"} "password")]
    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Login"))))
