(ns wb-handler.parser
  (:require [clojure.string :as str])
  (:use [net.cgrand.enlive-html])
  (:import java.net.URL))

(def url (URL. "http://weeklybeats.com/music/rss"))

(def rss-feed (atom (html-resource url)))

(defn update-rss-feed! []
  (swap! #(html-resource url) rss-feed))

(defn get-all []
  (select @rss-feed [:item]))

(defn item->week [item]
  (let [weekstr (->> (select item [:title])
                     (first)
                     (:content)
                     (first)
                     (re-find #"Week [0-9]+"))]
    (-> (str/split weekstr #"Week ") (second))))

(defn- resolve-title [s]
  (let [drop-i (->> (str/split s #" -") (take 2) (map count) (reduce +) (+ 4))]
    (->> (drop drop-i s) (apply str) str/trim)))

(defn parse-item [item]
  (let [week (item->week item)
        title (-> (select item [:title]) first :content first resolve-title)
        ;link (-> (select item [:link]))
        desc (->> (select item [:description]) first :content first (filter #(not= % \newline)) (apply str))
        date (-> (select item [:pubDate]) first :content first)
        guid (-> (select item [:guid]) first :content first)
        mp3 (-> (select item [:enclosure]) first :attrs :url)
        author (-> (select item [:itunes:author]) first :content first)
        explicit (-> (select item [:itunes:explicit]) first :content first)
        summary (->> (select item [:itunes:summary]) first :content first (filter #(not= % \newline)) (apply str))]
    {:week week
     :title title
     ;:link link
     :desc desc
     :date date
     :guid guid
     :mp3 mp3
     :author author
     :explicit explicit
     :summary summary}))

(defn item->filename [item]
  (last (str/split (:mp3 item) #"/")))

(defn get-week [i]
  (->> (get-all)
       (filter #(= (item->week %) (str i)))
       (map parse-item)))

(defn get-all-tracks [artist]
  (->> (get-all)
       (map parse-item)
       (filter #(= (:author %) artist))))
