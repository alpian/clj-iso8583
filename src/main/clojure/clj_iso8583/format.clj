(ns clj-iso8583.format
  (:use [clj-iso8583.binary :only [bytes-to-ascii]]))

(defn variable-length-field [length-of-length]
  (fn [input decoder]
    (let [[length-bytes remaining-input] (split-at length-of-length input)
          length (Integer/parseInt (bytes-to-ascii length-bytes))
          [field-bytes remaining-input] (split-at length remaining-input)]
      [(bytes-to-ascii field-bytes) remaining-input])))

(defn fixed-length-field [length]
  (fn [input decoder]
    (let [[field-bytes remaining-input] (split-at length input)]
      [(decoder field-bytes) remaining-input])))

(defn field-definition [field-number name reader & {:keys [decoder] :or {decoder bytes-to-ascii}}]
  [field-number {:name name :reader reader :decoder decoder}])

(defn make-field-definitions [descriptions]
  (let [make-field-definition #(apply field-definition %)]
    (into {} 
      (map make-field-definition descriptions))))
