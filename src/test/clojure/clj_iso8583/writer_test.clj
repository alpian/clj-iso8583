(ns clj-iso8583.writer-test
  (:require [clj-iso8583.binary :as binary]
            [clj-iso8583.format-iso8583 :as format-iso8583])
  (:use clojure.test midje.sweet clj-iso8583.writer))

(fact "Can write the message-type"
  (write (format-iso8583/field-definitions) {:message-type "0200"}) => "0200")

(fact "Sets the first bit in the primary bitmap for a secondary bitmap if there is a bit set in the secondary bitmap, like 66"
  (binary/binary-string-to-hex (write-bitmap [66])) => (str "8000000000000000" "4000000000000000"))

(fact "Sets the last bit in the primary bitmap for a tertiary bitmap if there is a bit set in the tertiary bitmap, like 129"
  (binary/binary-string-to-hex (write-bitmap [129])) => (str "8000000000000000" "8000000000000000" "8000000000000000"))

(fact "Can write a few fields"
  (binary/bytes-to-hex 
    (write (format-iso8583/field-definitions) 
      {:message-type "0200"
       :pan "1111222233334444"
       :processing-code "011000"
       :transaction-amount "000000006660"})) => 
    (str (binary/bytes-to-hex "0200") "7000000000000000" (binary/bytes-to-hex "161111222233334444011000000000006660")))

