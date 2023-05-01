(ns com.oakmac.iso8583.writer-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [com.oakmac.iso8583.binary :as binary]
    [com.oakmac.iso8583.format-iso8583 :as format-iso8583]
    [com.oakmac.iso8583.writer :as writer]))

(deftest write-test
  (testing "Can write the message-type"
    (is (= (writer/write (format-iso8583/field-definitions) {:message-type "0200"})
           "0200"))))

(deftest binary-test
  (testing "Sets the first bit in the primary bitmap for a secondary bitmap if there is a bit set in the secondary bitmap, like 66"
    (is (= (binary/binary-string-to-hex (writer/write-bitmap [66]))
           (str "8000000000000000" "4000000000000000"))))
  (testing "Sets the last bit in the primary bitmap for a tertiary bitmap if there is a bit set in the tertiary bitmap, like 129"
    (is (= (binary/binary-string-to-hex (writer/write-bitmap [129]))
           (str "8000000000000000" "8000000000000000" "8000000000000000")))))



(deftest write-fields-test
  (testing "Can write a few fields"
    (is (= (binary/bytes-to-hex
            (writer/write (format-iso8583/field-definitions)
                          {:message-type "0200"
                           :pan "1111222233334444"
                           :processing-code "011000"
                           :transaction-amount "000000006660"
                           ;; This is a 3 digit variable length field with
                           ;; length 17. Expect it to be 0 padded to 017.
                           :message-reason-code "because i said so"}))
           (str (binary/bytes-to-hex "0200") "7000000000000000" (binary/bytes-to-hex "161111222233334444011000000000006660017because i said so"))))))



; (deftest write-fields-test
;   (testing "Can write a few fields"
;     (is (= (binary/bytes-to-hex
;              (writer/write (format-iso8583/field-definitions)
;                {:message-type "0200"
;                 :pan "1111222233334444"
;                 :processing-code "011000"
;                 :transaction-amount "000000006660"
;                 :message-reason-code "mary had a little lamb"}))
;            (str (binary/bytes-to-hex "0200")
;                 "7000000000000000"
;                 (binary/bytes-to-hex "161111222233334444011000000000006660017mary had a little lamb"))))))



    ; (is (= (binary/bytes-to-hex
    ;         (writer/write (format-iso8583/field-definitions)
    ;                       {:message-type "0200"
    ;                        :pan "1111222233334444"
    ;                        :processing-code "011000"
    ;                        :transaction-amount "000000006660"}))
    ;                        ;; This is a 3 digit variable length field with
    ;                        ;; length 17. Expect it to be 0 padded to 017.

    ;        (str (binary/bytes-to-hex "0200")
    ;             "7000000000000000"
    ;             (binary/bytes-to-hex "161111222233334444011000000000006660017mary had a little lamb"))
    ;        "variable length fields get zero-padded"))))


; (not (= "3032303070000000000001003136303131313132323232333333333434343430313130303030303030303030303636363032323232306D617279206861642061206C6974746C65206C616D62"
;         "3032303070000000000000003136313131313232323233333333343434343031313030303030303030303030363636306D617279206861642061206C6974746C65206C616D62"))
