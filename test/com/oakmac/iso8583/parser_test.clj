(ns com.oakmac.iso8583.parser-test
  (:require
    [clj-iso8583.binary :as binary]
    [clj-iso8583.format :as format]
    [clj-iso8583.format-iso8583 :as format-iso8583]
    [clj-iso8583.parser :as parser]
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing]]))

(def full-message
  (str/join
    ["30323030f23e46d529e0910000000000" "10000022313635383133333930303036" "34333333323130313130303030303030" "30303030363636303130303931353032"
     "31393032383833383131303231393130" "30393135303931303039363031313035" "31303031303031324330303030303030" "30433030303030303030313131323430"
     "30313030303130333735383133333930" "30303634333333323144313530393232" "30363434303030303031303030313136" "30303230303030303030323230494e54"
     "3031323334494e543030303031303030" "31202020343638205448524553484f4c" "44204156454e55452020453531303620" "494e5420435459204f4e434131323474"
     "2b95779b5a223f303034313531303131" "30323030363030303330353031353531" "31323031353135303031303032303031" "32353260101480200000003130303030"
     "30303034373530474931303030333035" "53537243494e54303153536e6b202030" "32383833383032383833384949303830" "3930345354746c303352424332303132"
     "313030393030313830323138506f7374" "696c696f6e3a4d657461446174613233" "303138546674693a5549443131313231" "315445524d494e414c5f494431313131"
     "38546674693a5549443238303c546674" "695549443e3c5549443e544f5247534c" "47493130303033303553537230323030" "3a3032383833383a3130303931353032"
     "31393a3030363433333332313c2f5549" "443e3c2f546674695549443e32313154" "45524d494e414c5f4944323136494e54" "30313233342020202020202020303937"
     "37313749636344617461333936343c3f" "786d6c2076657273696f6e3d22312e30" "2220656e636f64696e673d225554462d" "38223f3e3c496363446174613e3c4963"
     "63526571756573743e3c416d6f756e74" "417574686f72697a65643e3030303030" "303030363135303c2f416d6f756e7441" "7574686f72697a65643e3c416d6f756e"
     "744f746865723e303030303030303030" "3030303c2f416d6f756e744f74686572" "3e3c4170706c69636174696f6e496e74" "65726368616e676550726f66696c653e"
     "313830303c2f4170706c69636174696f" "6e496e7465726368616e676550726f66" "696c653e3c4170706c69636174696f6e" "5472616e73616374696f6e436f756e74"
     "65723e303033363c2f4170706c696361" "74696f6e5472616e73616374696f6e43" "6f756e7465723e3c43727970746f6772" "616d3e33333946413532324644374646"
     "3135363c2f43727970746f6772616d3e" "3c5465726d696e616c436f756e747279" "436f64653e3132343c2f5465726d696e" "616c436f756e747279436f64653e3c54"
     "65726d696e616c566572696669636174" "696f6e526573756c743e383038303034" "383030303c2f5465726d696e616c5665" "72696669636174696f6e526573756c74"
     "3e3c5472616e73616374696f6e437572" "72656e6379436f64653e3132343c2f54" "72616e73616374696f6e43757272656e" "6379436f64653e3c5472616e73616374"
     "696f6e446174653e3130303431363c2f" "5472616e73616374696f6e446174653e" "3c5472616e73616374696f6e54797065" "3e30313c2f5472616e73616374696f6e"
     "547970653e3c556e7072656469637461" "626c654e756d6265723e374243433039" "37363c2f556e7072656469637461626c" "654e756d6265723e3c4170706c696361"
     "74696f6e4964656e7469666965723e41" "303030303030323737313031303c2f41" "70706c69636174696f6e4964656e7469" "666965723e3c43766d526573756c7473"
     "3e3032303330303c2f43766d52657375" "6c74733e3c4973737565724170706c69" "636174696f6e446174613e3031313041" "30303030333232303030303030303030"
     "3030303030303030303030303030463c" "2f4973737565724170706c6963617469" "6f6e446174613e3c5465726d696e616c" "4361706162696c69746965733e363034"
     "3032303c2f5465726d696e616c436170" "6162696c69746965733e3c4368697043" "6f6e646974696f6e436f64653e303c2f" "43686970436f6e646974696f6e436f64"
     "653e3c43727970746f6772616d496e66" "6f726d6174696f6e446174613e38303c" "2f43727970746f6772616d496e666f72" "6d6174696f6e446174613e3c2f496363"
     "526571756573743e3c2f496363446174" "613e3033494e54"]))

(deftest parser-extract-test
  (testing "Basic extraction"
    (let [parser (parser/parser (format-iso8583/field-definitions))
          msg (parser (binary/hex-to-bytes full-message))]
      (is (= (:message-type msg) "0200") "Can extract the message-type")
      (is (= (:pan msg) "5813390006433321") "Can extract the pan")
      (is (= (:processing-code msg) "011000") "Can extract the processing code")
      (is (= (:transaction-amount msg) "000000006660") "Can extract the transaction amount")
      (is (= (:transmission-date-time msg) "1009150219") "Can extract the transmission date-time")
      (is (= (:stan msg) "028838") "Can extract the stan")
      (is (= (:local-transaction-time msg) "110219") "Can extract the local transaction time")
      (is (= (:local-transaction-date msg) "1009") "Can extract the local transaction date")
      (is (= (:card-expiry-date msg) "1509") "Can extract the card expiry date")
      (is (= (:transaction-settlement-date msg) "1009") "Can extract the transaction settlement date")
      (is (= (:merchant-type msg) "6011") "Can extract the merchant type")
      (is (= (:pos-entry-mode msg) "051") "Can extract the pos entry mode")
      (is (= (:card-sequence-number msg) "001") "Can extract the card sequence number")
      (is (= (:pos-condition-code msg) "00") "Can extract the pos condition code")
      (is (= (:pos-capture-code msg) "12") "Can extract the pos capture code")
      (is (= (:transaction-fee-amount msg) "C00000000") "Can extract the transaction fee amount")
      (is (= (:transaction-processing-fee msg) "C00000000") "Can extract the transaction processing fee")
      (is (= (:acquiring-institution-id-code msg) "12400100010") "Can extract the acquiring institution id code")
      (is (= (:track-2 msg) "5813390006433321D15092206440000010001") "Can extract the track 2")
      (is (= (:retrieval-reference-number msg) "160020000000") "Can extract the retrieval reference number")
      (is (= (:service-restriction-code msg) "220") "Can extract the service restriction code")
      (is (= (:terminal-id msg) "INT01234") "Can extract the terminal id")
      (is (= (:card-acceptor-id msg) "INT000010001   ") "Can extract the card acceptor id")
      (is (= (:card-acceptor-name-location msg) "468 THRESHOLD AVENUE  E5106 INT CTY ONCA") "Can extract the card acceptor name location")
      (is (= (:transaction-currency msg) "124") "Can extract the transaction currency")
      (is (= (:pin-data msg) "742B95779B5A223F") "Can extract the pin data")
      (is (= (:message-reason-code msg) "1510") "Can extract the message reason code")
      (is (= (:receiving-institution-id-code msg) "02006000305") "Can extract the receiving institution id code")
      (is (= (:pos-data-code msg) "511201515001002") "Can extract the pos data code")))

  (testing "Can extract something from the tertiary bitmap"
    (let [parser (parser/parser (format/make-field-definitions [[130 :high-field (format/fixed-length-field 3)]]))
          msg (parser (binary/hex-to-bytes "30323030800000000000000080000000000000004000000000000000313233"))]
      (is (= (:high-field msg) "123")))))

(deftest parser-errors-test
  (testing "Too much data is reported as a validation error"
    (let [parser (parser/parser (format/make-field-definitions [[2 :field (format/fixed-length-field 3)]]))
          msg (parser (binary/hex-to-bytes "3032303040000000000000003132333435"))]
      (is (= (:field msg) "123"))
      (is (false? (:is-valid? msg)))
      (is (= (:errors msg) ["Trailing data found after message: '0x3435'"]))))

  (testing "A field too short is reported as a validation error"
    (let [parser (parser/parser (format/make-field-definitions [[2 :field (format/fixed-length-field 3)]]))
          msg (parser (binary/hex-to-bytes "3032303040000000000000003132"))]
      (is (false? (:is-valid? msg)))
      (is (= (:errors msg) ["(field-name) Error: Less than 3 bytes available. The data: [3132]"]))))

  (testing "When the message type is too short it is reported as a validation error"
    (let [parser (parser/parser (format/make-field-definitions []))
          msg (parser (binary/hex-to-bytes "303230"))]
      (is (false?(:is-valid? msg)))
      (is (= (:errors msg) ["(message-type) Error: Insufficient data. The data: [303230]"])))))
