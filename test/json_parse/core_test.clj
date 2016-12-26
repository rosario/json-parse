(ns json-parse.core-test
  (:require [clojure.xml :as xml]
            [clojure.test :refer :all]
            [json-parse.core :refer :all]))


(defn parse [s] (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))


(deftest a-test
  (testing "dirrent-keys?"
    (let [xml1 (parse "<data><h1> hello </h1> <h1> ciao </h1> </data>")
          xml2 (parse "<data><h1> hello </h1> <h2> ciao </h2> </data>")]
    (is (= false (different-keys? (:content xml1))))
    (is (= true  (different-keys? (:content xml2)))))))

; {:attrs nil, :content ["bravo"], :tag :hello}
(def xml-test1-input (parse "<hello>bravo</hello>"))

; {:attrs nil, :content [{:attrs nil, :content ["hello"], :tag :h1}], :tag :div}
(def xml-test2-input  (parse "<div><h1>hello</h1></div>"))

; Sequence of the same tags
; {:attrs nil,
;  :content [{:attrs nil, :content ["Salvatore"], :tag :person}
;            {:attrs nil, :content ["Giuseppe"],  :tag :person}],
;  :tag :persons}
(def xml-test3-input
  (parse "<persons><person>Salvatore</person><person>Giuseppe</person></persons>"))

; Sequence of tags, but different attributes
; {:attrs nil,
;  :content [{:attrs nil, :content ["Salvatore"], :tag :name}
;            {:attrs nil, :content ["Parlermo"],  :tag :address}],
;  :tag :person}
(def xml-test4-input
  (parse "<person><name>Salvatore</name><address>Parlermo</address></person>"))

; Attributes, but no content
;{:attrs {:address "Erice", :name "Concetta"},
;         :content nil, :tag :person}
(def xml-test5-input
  (parse "<person name=\"Concetta\" address=\"Erice\"> </person> "))

(deftest a-test
  (testing "xml->json"
    (is (= nil     (xml->json nil)))            ; Base case
    (is (= "hello" (xml->json "hello")))        ; Just one string
    (is (= {}      (xml->json {})))             ; Empty map
    ; Simple map with just one tag and content of String
    (is (= {:hello "bravo"}
           (xml->json xml-test1-input)))
    ; One nested tag with String content
    (is (= {:div {:h1 "hello"}}
           (xml->json xml-test2-input)))
    ; Two nested element, same tag
    (is (= {:persons [{:person "Salvatore"} {:person "Giuseppe"}]}
           (xml->json xml-test3-input)))
    ; Two nested element, different tag
    (is (= {:person {:address "Parlermo", :name "Salvatore"}}
           (xml->json xml-test4-input)))
    ; No content, but attributes
    (is (= {:person nil, :personAttrs {:address "Erice", :name "Concetta"}}
           (xml->json xml-test5-input)))
    )
  )