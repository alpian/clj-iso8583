(ns clj-iso8583.binary-test
  (:import [java.io File]
           [java.nio ByteBuffer ByteOrder]
           [java.util Date])
  (:use clj-iso8583.binary clojure.test midje.sweet))

(fact (hex-to-bytes "010203") => [1 2 3])

(fact (bytes-to-hex (long-to-bytes (long 1))) => "0000000000000001")
(fact (bytes-to-hex (long-to-bytes (long 2r0001110010111010100110000111011001010100001000111001100100010001))) => "1CBA987654239911")

(fact "F23E46D529E09100" => (bytes-to-hex (bitmap [1 2 3 4 7 11 12 13 14 15 18 22 23 25 26 28 30 32 35 37 40 41 42 43 49 52 56])))
(fact "FFFFFFFFFFFFFFFF" => (bytes-to-hex (bitmap (range 1 65 1))))

(fact 
  (set-bits (byte 0x35)) => [0 2 4 5]
  (set-bits (byte 0x42)) => [1 6])

(fact 
  (set-bits (hex-to-bytes "42 10 01")) => [0 12 17 22]
  (little-endian-set-bits (hex-to-bytes "42 10 00 11 02 C0 48 04")) => [2 7 12 28 32 39 41 42 50 53 62])
