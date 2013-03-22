(ns clj-iso8583.binary-test
  (:import [java.io File]
           [java.nio ByteBuffer ByteOrder]
           [java.util Date])
  (:require [clj-iso8583.binary :as binary])
  (:use clojure.test
        midje.sweet))

(fact (binary/hex-to-bytes "010203") => [1 2 3])

(fact 
  (binary/set-bits (byte 0x35)) => [0 2 4 5]
  (binary/set-bits (byte 0x42)) => [1 6])

(fact 
  (binary/set-bits (binary/hex-to-bytes "42 10 01")) => [0 12 17 22]
  (binary/little-endian-set-bits (binary/hex-to-bytes "42 10 00 11 02 C0 48 04")) => [2 7 12 28 32 39 41 42 50 53 62])
