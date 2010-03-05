(ns com.davidsantiago.clojure-hbase.util
  (:refer-clojure :rename {get map-get})
  (:import org.apache.hadoop.hbase.util.Bytes))

;; Utility functions that may be helpful in using the library.

(defn as-kw
  "Takes a byte-array and turns it into a keyword (assuming it can be turned
   into a string first."
  [arg]
  (keyword (Bytes/toString arg)))

(defn as-str
  "Takes a byte-array and turns it into a string."
  [arg]
  (Bytes/toString arg))

(defn as-sym
  "Takes a byte-array and turns it into a symbol."
  [arg]
  (symbol (Bytes/toString arg)))