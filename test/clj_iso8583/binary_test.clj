(ns clj-iso8583.binary-test
  (:import [java.io File]
           [java.nio ByteBuffer ByteOrder]
           [java.util Date])
  (:require [clj-iso8583.binary :as binary])
  (:use clojure.test
        midje.sweet))

(fact (binary/hex-to-bytes "010203") => [1 2 3])

(fact 
  (binary/set-bits (byte 0x35)) => [0 2 4 5]
  (binary/set-bits (byte 0x42)) => [1 6])

(fact 
  (binary/all-set-bits (binary/hex-to-bytes "42 10 01")) => [0 12 17 22]
  (binary/little-endian-all-set-bits (binary/hex-to-bytes "42 10 00 11 02 C0 48 04")) => [2 7 12 28 32 39 41 42 50 53 62])

(defn field-definition-of [index field-definitions]
  (first (filter (fn [[key value]] (= index (:index value))) field-definitions)))

(defn extract-field 
  [message field-definition]
  (subs message 0 (:length (second field-definition))))

(defn remaining-after 
  [message field-definition]
  (subs message (:length (second field-definition))))

(defn parse-message
  [message set-field-indices field-definitions]
  (lazy-seq
    (when (not-empty set-field-indices)
      (let [field-definition (field-definition-of (first set-field-indices) field-definitions)]
        (cons (extract-field message field-definition) 
              (parse-message (remaining-after message field-definition) (rest set-field-indices) field-definitions))))))

;(parse-message "AABBCC" [1 2 3] {:one {:index 1 :length 1}, :two {:index 2 :length 2}, :three {:index 3 :length 3}})
