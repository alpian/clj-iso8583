(ns com.oakmac.iso8583.writer
  (:require
    [com.oakmac.iso8583.binary :as binary]
    [com.oakmac.iso8583.format-iso8583 :as format-iso8583]))

(defn- find-first [pred coll]
  (some #(when (pred %) %) coll))

(defn- field-name-of [field-definition]
  (:name (second field-definition)))

(defn- bits-in-range [low high bits]
  (filter #(and (> % low) (< % high)) bits))

(defn- bitmap-string [bits]
  (binary/bytes-to-string (binary/little-endian-bitmap (sort bits))))

(defn- flag-next-bitmap-set [this-bitmap bits-from-next-bitmap]
  (concat (when bits-from-next-bitmap [1]) this-bitmap))

(defn write-bitmap [set-bits]
  (let [tertiary-bitmap-bits (seq (map #(- % 128) (bits-in-range 128 193 set-bits)))
        secondary-bitmap-bits (seq (flag-next-bitmap-set (map #(- % 64) (bits-in-range 64 129 set-bits)) tertiary-bitmap-bits))
        primary-bitmap-bits (flag-next-bitmap-set (bits-in-range 0 65 set-bits) secondary-bitmap-bits)]
    (str 
      (bitmap-string primary-bitmap-bits)
      (when secondary-bitmap-bits (bitmap-string secondary-bitmap-bits))
      (when tertiary-bitmap-bits (bitmap-string tertiary-bitmap-bits)))))

(defn write [field-definitions fields] 
  (str 
    (:message-type fields)
    (if-let [present-field-definitions (seq (sort-by first (filter #(contains? fields (field-name-of %)) (seq field-definitions))))]
      (apply str
        (write-bitmap (map first present-field-definitions))
        (for [field-definition present-field-definitions
              :let [field-descriptors (second field-definition)
                    value ((:name field-descriptors) fields)]]
          ((:writer field-descriptors) value))))))
