(ns clj-iso8583.binary
  (:import java.nio.charset.Charset))

(defn ubyte
  "Coerce to unsigned byte"
  {:tag Byte
   :inline (fn  [x] `(. com.ind.clojure.Binary (unsignedByteCast ~x)))}
  [#^Number x]
  (. x (byteValue)))

(defn bytes-to-ascii [bytes]
  (new String (byte-array bytes) (Charset/forName "ISO-8859-1")))

(defn bytes-to-string [array-of-bytes]
  (new String array-of-bytes (Charset/forName "ISO-8859-1")))

(defn bytes-to-hex [bytes]
  (apply str (map #(format "%02X" (byte %)) bytes)))

(defn long-to-bytes [number]
  (byte-array (map #(ubyte (bit-and 0xFF (bit-shift-right number %))) (reverse (range 0 64 8)))))

(defn bitmap [set-bits]
  (long-to-bytes (reduce #(bit-or %1 %2) (map #(bit-shift-left 1 (- 64 %)) set-bits))))

(defn hex-to-bytes [hex]
  (map #(ubyte (Integer/parseInt % 16)) (re-seq #"[0-9A-Fa-f]{2}" hex)))

(defmulti set-bits (fn [input] (if (instance? Iterable input) :iterable :single-byte)))

(defmethod set-bits :single-byte [byte]
  (keep-indexed #(if %2 %1) (map #(bit-test byte %) (range 0 8))))

(defmethod set-bits :iterable [bytes]
  (let [number-of-bytes (count bytes)
        offset-for (fn [index] (- (* (- number-of-bytes 1) 8) (* index 8)))] 
    (-> (keep-indexed (fn [index item] (let [offset (offset-for index)] (map #(+ % offset) (set-bits item)))) bytes)
        flatten  
        sort)))

(defn little-endian-set-bits [bytes]
  (sort (map #(- (* 8 (count bytes)) %) (set-bits bytes)))) 