

(defproject clojure-hbase "0.20.2"
  :description "A convenient Clojure interface to HBase."
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [org.apache.mahout.hadoop/hadoop-core "0.20.1"]
                 [org.apache.zookeeper/zookeeper "3.3.2"]
                 [org.apache.hbase/hbase "0.90.1"]
                 [log4j/log4j "1.2.15"
                  :exclusions [javax.mail/mail
                             javax.jms/jms
                             com.sun.jdmk/jmxtools
                             com.sun.jmx/jmxri]]
		 [commons-logging/commons-logging "1.1"]])

(ns leiningen.docs)
;(defn docs...)
