(ns clj-iso8583.format
  (:use [clj-iso8583.binary :only [bytes-to-ascii]]))

(defn variable-length-field [length-of-length] 
  {:reader 
     (fn [input decoder]
       (let [[length-bytes remaining-input] (split-at length-of-length input)
             length (Integer/parseInt (bytes-to-ascii length-bytes))
             [field-bytes remaining-input] (split-at length remaining-input)]
         [(bytes-to-ascii field-bytes) remaining-input]))
   :writer 
     (fn [value encoder] 
       (str (count value) (encoder value)))
   })

(defn fixed-length-field [length]
  {:reader 
     (fn [input decoder]
       (let [[field-bytes remaining-input] (split-at length input)]
         [(decoder field-bytes) remaining-input]))
   :writer 
     (fn [value encoder] (encoder value))
   })

(defn field-definition 
  [field-number 
   name
   {reader :reader writer :writer} & 
   {:keys [codec] :or {codec {:decoder bytes-to-ascii 
                              :encoder identity}}}]
  [field-number 
   {:name name
    :reader reader 
    :writer writer 
    :decoder (:decoder codec) 
    :encoder (:encoder codec)}])

(defn make-field-definitions [descriptions]
  (let [make-field-definition #(apply field-definition %)]
    (into {} (map make-field-definition descriptions))))
