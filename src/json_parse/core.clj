(ns json-parse.core
  (:require [clojure.xml :as xml]))


(defn different-keys? [content]
  (when content
    (let [dkeys (count (filter identity (distinct (map :tag content))))
          n (count content)]
      (= dkeys n))))


(defn xml->json [element]
  (cond
    (nil? element) nil
    (string? element) element
    (sequential? element) (if (> (count element) 1)
                           (if (different-keys? element)
                             (reduce into {} (map (partial xml->json ) element))
                             (map xml->json element))
                           (xml->json  (first element)))
    (and (map? element) (empty? element)) {}
    (map? element) (if (:attrs element)
                    {(:tag element) (xml->json (:content element))
                     (keyword (str (name (:tag element)) "Attrs")) (:attrs element)}
                    {(:tag element) (xml->json  (:content element))})
    :else nil))