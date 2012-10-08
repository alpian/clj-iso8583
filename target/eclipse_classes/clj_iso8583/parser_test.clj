(ns clj-iso8583.parser-test
  (:import [java.io File]
           [java.nio ByteBuffer ByteOrder]
           [java.util Date])
  (:use clojure.test
        midje.sweet
        clj-iso8583.core))

(defn hex-to-bytes
  "Converts a given hex string to a byte-array and returns the byte-array"
  [hex]
  (map #(Integer/parseInt % 16) (re-seq #"[0-9A-Fa-f]{2}" hex)))

(defn parse-iso-message
  "Parses an ISO message and returns a map of all the fields of that message"
  [input]
  (let [bitmap (first input)]
    bitmap))

(deftest can-read-bitmaps
  (testing "Can read a field from a bitmap"
    (is (= "1234" (:pan (parse-iso-message (hex-to-bytes "401234")))))))

(deftest can-read-bitmaps-and-test-with-midje
  (fact "can read bitmaps"
    (:pan (parse-iso-message (hex-to-bytes "401234"))) => "1234"))

(defn indices-of-set-bits
  "Little-endian indices of all the bits set in a byte" 
  [byte] 
  (keep-indexed #(if %2 (- 8 %1)) (map #(bit-test byte %) (range 0 8))))

(defn set-bits-in-a-bitmap 
  "Little-endian indices of all the bits set in a byte" 
  [bytes] 
  (sort (flatten (keep-indexed (fn [index item] (map #(+ % (* index 8)) (indices-of-set-bits item))) bytes))))

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
