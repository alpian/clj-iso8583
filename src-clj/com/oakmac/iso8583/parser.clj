(ns com.oakmac.iso8583.parser
  (:require
    [com.oakmac.iso8583.binary :as binary]))

(defn- error [field-name error-message data]
  {:errors [(str "(" (name field-name) ") Error: " error-message ". The data: [" data "]")]})

(defn message-type-of [input]
  (if (>= (count input) 4)
    (let [[message-type-bytes remaining-input] (split-at 4 input)]
      [{:message-type (binary/bytes-to-ascii message-type-bytes)} remaining-input])
    [(error :message-type "Insufficient data" (binary/bytes-to-hex input)) nil]))

(defn- field-set? [bit-index bitmap] 
  (some #{bit-index} bitmap))

(defn- read-bitmap [input offset]
  (let [[bitmap-bytes remaining-input] (split-at 8 input)
        bitmap (binary/little-endian-set-bits bitmap-bytes)]
    [(map #(+ % offset) bitmap) remaining-input]))

(defn- validate-trailing-data [input]
  (when (not (= 0 (count input))) 
    {:errors [(format "Trailing data found after message: '0x%s'" (binary/bytes-to-hex input))]}))

(defn bitmap-of [input]
  (let [[all-bits remaining-input]
        (let [[primary-bits remaining-input] (read-bitmap input 0)]
          (if (field-set? 1 primary-bits)
            (let [[secondary-bits remaining-input] (read-bitmap remaining-input 64)]
              (if (field-set? 65 secondary-bits)
                (let [[tertiary-bits remaining-input] (read-bitmap remaining-input 128)]
                    [(concat primary-bits secondary-bits tertiary-bits) remaining-input])
                [(concat primary-bits secondary-bits) remaining-input]))
            [primary-bits remaining-input]))]
    [(remove #{1 65} all-bits) remaining-input]))

(defn- no-errors? [potential]
  (empty? (:errors potential)))

(defn parse-fields [field-definitions bitmap input]
  (loop [field-number (first bitmap)
         bitmap (rest bitmap)
         remaining-input input
         fields {}]
    (if-let [field-definition (get field-definitions field-number)]
        (let [[parsed-field remaining-input] ((:reader field-definition) remaining-input)]
          (recur 
            (first bitmap) 
            (rest bitmap) 
            remaining-input 
            (if (no-errors? parsed-field) 
              (assoc fields (:name field-definition) parsed-field) 
              (merge-with concat fields parsed-field))))
        [fields remaining-input])))

(defn parse-bitmap-message [field-definitions input]
  (let [[bitmap remaining-input] (bitmap-of input)]
    (parse-fields field-definitions bitmap remaining-input)))

(defn parse-full-message
  "Parses an ISO message and returns a map of all the fields of that message"
  [field-definitions input]
  (let [[message-type remaining-input] (message-type-of input)
        [fields remaining-input] (when (empty? (:errors message-type)) (parse-bitmap-message field-definitions remaining-input))
        all-fields (merge-with concat message-type fields (validate-trailing-data remaining-input))]
    (if (no-errors? all-fields)
      all-fields
      (merge {:is-valid? false} all-fields))))

(defn parser [field-definitions] (partial parse-full-message field-definitions))
