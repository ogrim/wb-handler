(ns wb-handler.core
  (:require [clojure.string :as str]
            [wb-handler.db :as db]))

(defn insert-week
  "Insert week i into sqlite database.db"
  [i]
  (->> (get-week i)
       (map parse-item)
       (map db/insert)))

(defn download-week
  "Download entire week i, creates neccessary folders"
  [i]
  (;not implemented yet
   ))


(def favorite-artists ["some" "artists"])
(defn download-favorites-week [i]
  ;not implemented yet
  )

;(loop [i 0, [item & more] (map parse-item (get-week 1))] (if (= (:author item) "NWSPR") i (recur (inc i) more)))
