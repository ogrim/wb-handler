(ns wb-handler.core
  (:require [clojure.string :as str]
            [wb-handler.db :as db]
            [wb-handler.parser :as p]
            [clojure.java.io :as io])
  (:import [java.io.FileOutputStream]))

(def ^{:dynamic true} *favorite-artists*
  #{"little-scale" "Dos.Putin" "Falling For A Square" "cTrix" "midimachine"
    "minikomi" "Derris-Kharlan" "kedromelon" "Ryan" "trash80"})

(defn in? [seq elm]
  (if (some #{elm} seq) true false))

(defn make-dir [week]
  (let [folder (str "weeklybeats/" week "/")
        _ (-> (str folder "dummy") io/file io/make-parents)]
    folder))

(defn file-exists? [path]
  (. (io/file path) exists))

(defn download
  "Download binary file from uri to file"
  [uri file]
  (do (with-open [in (io/input-stream uri)
               out (io/output-stream file)]
     (io/copy in out))
      true))

(defn available-weeks
  "What weeks are available in the RSS feed?"
  []
  (->> (p/get-all)
       (map p/item->week)
       (distinct)
       (sort)
       (map #(Integer/parseInt %))))

(defn insert-week
  "Insert weekly data into sqlite database.db"
  [i]
  (->> (p/get-week i)
       (map db/insert)))

(defn download-favorites-week
  "Download the tracks for the artists in *favorite-artists* for week i, or the artists specified"
  ([i]
     (let [path (make-dir i)]
       (->> (p/get-week i)
            (filter #(in? *favorite-artists* (:author %)))
            (doall)
            (filter #(not (file-exists? (str path (p/item->filename %)))))
            (map #(download (:mp3 %) (str path (p/item->filename %)))))))
  ([i artists]
  (binding [*favorite-artists* artists]
    (download-favorites-week i))))

(defn download-week
  "Download entire week i, creates neccessary folders"
  [i]
  (let [path (make-dir i)]
    (->> (p/get-week i)
         (filter #(not (file-exists? (str path (p/item->filename %)))))
         (map #(download (:mp3 %) (str path (p/item->filename %)))))))

(defn download-all-artist
  "Download all tracks from single artist in RSS into own artist folder"
  [artist]
  (let [path (make-dir (str "artists/" artist))]
    (->> (p/get-all-tracks artist)
         (filter #(not (file-exists? (str path (p/item->filename %)))))
         (map #(download (:mp3 %) (str path (p/item->filename %)))))))
