(ns com.oakmac.iso8583.binary-test
  (:require
    [clojure.test :refer [deftest is]]
    [com.oakmac.iso8583.binary :as binary]))

(deftest ubyte-test
  (is (= (binary/ubyte 0) 0))
  (is (= (binary/ubyte 24) 24))
  (is (= (binary/ubyte 100) 100))
  (is (= (binary/ubyte 164) -92))
  (is (= (binary/ubyte 212) -44))
  (is (= (binary/ubyte 255) -1))
  (is (thrown-with-msg? IllegalArgumentException #"Value out of range" (binary/ubyte -1)))
  (is (thrown-with-msg? IllegalArgumentException #"Value out of range" (binary/ubyte 256))))

(deftest hex-to-bytes-test
  (is (= (binary/hex-to-bytes "010203")
         [1 2 3])))

(deftest long-to-bytes-test
  (is (= (binary/bytes-to-hex (binary/long-to-bytes (long 1)))
         "0000000000000001"))
  (is (= (binary/bytes-to-hex (binary/long-to-bytes (long 2r0001110010111010100110000111011001010100001000111001100100010001)))
         "1CBA987654239911")))

(deftest little-endian-bitmap-test
  (is (= (binary/bytes-to-hex (binary/little-endian-bitmap [1]))
         "8000000000000000"))  
  (is (= (binary/bytes-to-hex (binary/little-endian-bitmap [1 2 3 4 7 11 12 13 14 15 18 22 23 25 26 28 30 32 35 37 40 41 42 43 49 52 56]))
         "F23E46D529E09100"))
  (is (= (binary/bytes-to-hex (binary/little-endian-bitmap (range 1 65 1)))
         "FFFFFFFFFFFFFFFF")))

(deftest set-bits-test
  (is (= (binary/set-bits (byte 0x35))
         [0 2 4 5]))
  (is (= (binary/set-bits (byte 0x42))
         [1 6]))
  (is (= (binary/set-bits (binary/hex-to-bytes "42 10 01"))
         [0 12 17 22]))
  (is (= (binary/little-endian-set-bits (binary/hex-to-bytes "42 10 00 11 02 C0 48 04"))
         [2 7 12 28 32 39 41 42 50 53 62])))
