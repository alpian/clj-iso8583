(ns clj-iso8583.parser
  (:require [clj-iso8583.binary :as binary]))

(defn message-type-of [input]
  (let [[message-type-bytes remaining-input] (split-at 4 input)]
    [(binary/bytes-to-ascii message-type-bytes) remaining-input]))

(defn- field-set? [bit-index bitmap] 
  (some #{bit-index} bitmap))

(defn bitmap-of [input]
  (let [[primary-bitmap-bytes rest] (split-at 8 input)
        primary-bitmap (binary/little-endian-set-bits primary-bitmap-bytes)]
    (if (field-set? 1 primary-bitmap) 
          (let [[secondary-bitmap-bytes rest] (split-at 8 rest)
                secondary-bitmap (binary/little-endian-set-bits secondary-bitmap-bytes)]
            [(concat (remove #{1} primary-bitmap) (map #(+ % 64) secondary-bitmap)) rest])
          [primary-bitmap rest])))

(defn parse-fields [field-definitions bitmap input]
  (loop [field-number (first bitmap)
         bitmap (rest bitmap)
         remaining-input input
         fields {}]
    (if-let [field-definition (get field-definitions field-number)]
        (let [reader (:reader field-definition)
              decoder (:decoder field-definition)
              [field-content remaining-input] (reader remaining-input decoder)]
          (recur (first bitmap) (rest bitmap) remaining-input (assoc fields (:name field-definition) field-content)))
        [fields remaining-input])))

(defn parse
  "Parses an ISO message and returns a map of all the fields of that message"
  [field-definitions input]
  (let [[message-type remaining-input] (message-type-of input)
        [bitmap remaining-input] (bitmap-of remaining-input)
        [fields remaining-input] (parse-fields field-definitions bitmap remaining-input)
        parsed-message (assoc fields :message-type message-type)]
    parsed-message))
