(ns clojure-hbase.admin
  (:refer-clojure :rename {get map-get} :exclude [flush])
  (:use clojure-hbase.core
        clojure-hbase.internal)
  (:import [org.apache.hadoop.hbase HBaseConfiguration HConstants
            HTableDescriptor HColumnDescriptor]
           [org.apache.hadoop.hbase.client HBaseAdmin]
           [org.apache.hadoop.hbase.util Bytes]
           [org.apache.hadoop.hbase.io.hfile Compression]))

(def ^{:tag HBaseAdmin :dynamic true} *admin*
  (delay (HBaseAdmin. (HBaseConfiguration/create))))

(defn get-admin []
  (if (instance? clojure.lang.IDeref *admin*) @*admin* *admin*))

;;
;; HColumnDescriptor
;;

;; This maps each get command to its number of arguments, for helping us
;; partition the command sequence.
(def ^{:private true} column-desc-argnums
  {:block-cache-enabled      1   ;; :block-cache-enabled <boolean>
   :block-size               1   ;; :block-size <int>
   :bloom-filter-type        1   ;; :bloom-filter <StoreFile.BloomType>
   :compression-type         1   ;; :compression-type <Compression.Algorithm>
   :in-memory                1   ;; :in-memory <boolean>
   :max-versions             1   ;; :max-versions <int>
   :time-to-live             1}) ;; :time-to-live <int>

(defn column-descriptor
  [family-name & args]
  (let [specs (partition-query args column-desc-argnums)
        ^HColumnDescriptor cd (HColumnDescriptor. (to-bytes family-name))]
    (doseq [spec specs]
      (condp = (first spec)
          :block-cache-enabled      (.setBlockCacheEnabled cd (second spec))
          :block-size               (.setBlocksize cd (second spec))
          :bloom-filter-type        (.setBloomFilterType cd (second spec))
          :compression-type         (.setCompressionType cd (second spec))
          :in-memory                (.setInMemory cd (second spec))
          :max-versions             (.setMaxVersions cd (second spec))
          :time-to-live             (.setTimeToLive cd (second spec))))
    cd))

;;
;; HTableDescriptor
;;

;; This maps each get command to its number of arguments, for helping us
;; partition the command sequence.
(def ^{:private true} table-desc-argnums
  {:max-file-size         1   ;; :max-file-size <long>
   :mem-store-flush-size  1   ;; :mem-store-flush-size <long>
   :read-only             1   ;; :read-only <boolean>
   :family                1}) ;; :family <HColumnDescriptor>

(defn table-descriptor
  [table-name & args]
  (let [specs (partition-query args table-desc-argnums)
        td (HTableDescriptor. (to-bytes table-name))]
    (doseq [spec specs]
      (condp = (first spec)
          :max-file-size         (.setMaxFileSize td (second spec))
          :mem-store-flush-size  (.setMemStoreFlushSize td (second spec))
          :read-only             (.setReadOnly td (second spec))
          :family                (.addFamily td (second spec))))
    td))


;;
;; HBaseAdmin
;;

(defn add-column-family
  [table-name ^HColumnDescriptor column-descriptor]
  (.addColumn (get-admin) (to-bytes table-name) column-descriptor))

(defn hbase-available?
  []
  (HBaseAdmin/checkHBaseAvailable (HBaseConfiguration.)))

(defn compact
  [table-or-region-name]
  (.compact (get-admin) (to-bytes table-or-region-name)))

(defn create-table
  [table-descriptor]
  (.createTable (get-admin) table-descriptor))

(defn create-table-async
  [^HTableDescriptor table-descriptor split-keys]
  (.createTableAsync (get-admin) table-descriptor split-keys))

(defn delete-column-family
  [table-name column-name]
  (.deleteColumn (get-admin) (to-bytes table-name) (to-bytes column-name)))

(defn delete-table
  [table-name]
  (.deleteTable (get-admin) (to-bytes table-name)))

(defn disable-table
  [table-name]
  (.disableTable (get-admin) (to-bytes table-name)))

(defn enable-table
  [table-name]
  (.enableTable (get-admin) (to-bytes table-name)))

(defn flush
  [table-or-region-name]
  (.flush (get-admin) (to-bytes table-or-region-name)))

(defn cluster-status
  []
  (.getClusterStatus (get-admin)))

(defn get-connection
  []
  (.getConnection (get-admin)))

(defn get-master
  []
  (.getMaster (get-admin)))

(defn get-table-descriptor
  [table-name]
  (.getTableDescriptor (get-admin) (to-bytes table-name)))

(defn master-running?
  []
  (.isMasterRunning (get-admin)))

(defn table-available?
  [table-name]
  (.isTableAvailable (get-admin) (to-bytes table-name)))

(defn table-disabled?
  [table-name]
  (.isTableDisabled (get-admin) (to-bytes table-name)))

(defn table-enabled?
  [table-name]
  (.isTableEnabled (get-admin) (to-bytes table-name)))

(defn list-tables
  []
  (seq (.listTables (get-admin))))

(defn major-compact
  [table-or-region-name]
  (.majorCompact (get-admin) (to-bytes table-or-region-name)))

(defn modify-column-family
  [table-name column-name ^HColumnDescriptor column-descriptor]
  (.modifyColumn (get-admin) (to-bytes table-name) (to-bytes column-name)
                 column-descriptor))

(defn modify-table
  [table-name table-descriptor]
  (.modifyTable (get-admin) (to-bytes table-name) table-descriptor))

(defn shutdown
  []
  (.shutdown (get-admin)))

(defn split
  [table-or-region-name]
  (.split (get-admin) (to-bytes table-or-region-name)))

(defn table-exists?
  [table-name]
  (.tableExists (get-admin) (to-bytes table-name)))
