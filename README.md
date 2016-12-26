# json-parse

Convert XML to JSON in Clojure

## Usage

First we need to parse the XML with this function:

```clojure
    (defn parse [s] (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))
```


Then we just use the `xml->json`:

```clojure
    (def parsed-xml (parse "<person><name>Salvatore</name><address>Parlermo</address></person>"))
    (def json (xml->json parsed-xml)
```
    

