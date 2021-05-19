(ns bloggo.admin)

(def admin-login (or (System/getenv "CLJBLOGGO_ADMIN_LOGIN") "admin"))
(def admin-password (or (System/getenv "CLGBLOGGO_ADMIN_PASSWORD") "hunter2"))

(defn check-login [login password]
  (and (= login admin-login)
       (= password admin-password)))
