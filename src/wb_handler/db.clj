(ns wb-handler.db
  (:require [clojure.java.jdbc :as sql]))

(def sqlite
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "database.db"})

(defn create-db []
  (try (sql/with-connection sqlite
         (sql/create-table "weeklybeats"
                           [:date :text]
                           [:desc :text]
                           [:author :text]
                           [:explicit :text]
                           [:guid :text]
                           [:title :text]
                           [:week :text]
                           [:summary :text]
                           [:mp3 :text]))
       (catch Exception e (println e))))

(defn insert [item-map]
  (sql/with-connection sqlite
    (sql/insert-records "weeklybeats" item-map)))
