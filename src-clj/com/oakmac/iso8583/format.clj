(ns com.oakmac.iso8583.format
  (:require
    [com.oakmac.iso8583.binary :refer [bytes-to-ascii bytes-to-hex]]
    [com.oakmac.iso8583.util.string :as util.str]))

(defn variable-length-field [field-length]
  {:reader
   (fn [_decoder-fn _field-name input]
     (let [[length-bytes remaining-input] (split-at field-length input)
           length (Integer/parseInt (bytes-to-ascii length-bytes))
           [field-bytes remaining-input] (split-at length remaining-input)]
       [(bytes-to-ascii field-bytes) remaining-input]))

   :writer
   (fn [encoder-fn _field-name value]
     (str
      (util.str/left-pad (str (count value)) field-length "0")
      (encoder-fn value)))})

(defn- error [field-name error-msg data]
  {:errors [(str "(field=" (name field-name) ") Error: " error-msg ". The data: [" data "]")]})

(defn fixed-length-field [length]
  {:reader
   (fn [decoder-fn field-name input]
     (if (>= (count input) length)
       (let [[field-bytes remaining-input] (split-at length input)]
         [(decoder-fn field-bytes) remaining-input])
       [(error field-name (str "Less than " length " bytes available") (bytes-to-hex input))]))

   :writer
   (fn [encoder-fn _field-name value]
     (encoder-fn value))})

(defn field-definition
  [field-number
   field-name
   {reader :reader writer :writer} &
   {:keys [codec] :or {codec {:decoder bytes-to-ascii
                              :encoder identity}}}]
  (let [{:keys [decoder encoder]} codec]
    [field-number
     {:name field-name
      :reader (partial reader decoder field-name)
      :writer (partial writer encoder field-name)}]))

(defn make-field-definitions [descriptions]
  (let [make-field-definition #(apply field-definition %)]
    (into {} (map make-field-definition descriptions))))
