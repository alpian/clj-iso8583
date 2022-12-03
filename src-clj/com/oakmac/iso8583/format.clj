(ns com.oakmac.iso8583.format
  (:require
    [com.oakmac.iso8583.binary :refer [bytes-to-ascii bytes-to-hex]]))

(defn variable-length-field [length-of-length]
  {:reader
   (fn [decoder input]
     (let [[length-bytes remaining-input] (split-at length-of-length input)
           length (Integer/parseInt (bytes-to-ascii length-bytes))
           [field-bytes remaining-input] (split-at length remaining-input)]
       [(bytes-to-ascii field-bytes) remaining-input]))

   :writer
   (fn [encoder value]
     (str (count value) (encoder value)))})

(defn- error [field-name error-message data]
  {:errors [(str "(" (name field-name) ") Error: " error-message ". The data: [" data "]")]})

(defn fixed-length-field [length]
  {:reader 
   (fn [decoder input]
     (if (>= (count input) length)
       (let [[field-bytes remaining-input] (split-at length input)]
         [(decoder field-bytes) remaining-input])
       [(error "field-name" (str "Less than " length " bytes available") (bytes-to-hex input))]))

   :writer
   (fn [encoder value] (encoder value))})

(defn field-definition 
  [field-number 
   name
   {reader :reader writer :writer} & 
   {:keys [codec] :or {codec {:decoder bytes-to-ascii 
                              :encoder identity}}}]
  (let [{:keys [decoder encoder]} codec]
    [field-number 
     {:name name
      :reader (partial reader decoder) 
      :writer (partial writer encoder)}]))

(defn make-field-definitions [descriptions]
  (let [make-field-definition #(apply field-definition %)]
    (into {} (map make-field-definition descriptions))))
