(ns wb-handler.parser
  (:require [clojure.string :as str])
  (:use [net.cgrand.enlive-html])
  (:import java.net.URL))

(def rss-feed (html-resource (URL. "http://weeklybeats.com/music/rss")))

(defn get-all []
  (select rss-feed [:item]))

(defn item->week [item]
  (let [weekstr (->> (select item [:title])
                     (first)
                     (:content)
                     (first)
                     (re-find #"Week [0-9]+"))]
    (-> (str/split weekstr #"Week ") (second))))

(defn get-week [i]
  (->> (get-all)
       (filter #(= (item->week %) (str i)))))

(defn resolve-title [s]
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
