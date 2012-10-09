(ns clj-iso8583.binary)

(defn hex-to-bytes [hex]
  (map #(Integer/parseInt % 16) (re-seq #"[0-9A-Fa-f]{2}" hex)))


(defn set-bits [byte] 
  (keep-indexed #(if %2 %1) (map #(bit-test byte %) (range 0 8))))

(defn all-set-bits [bytes] 
  (let [number-of-bytes (count bytes)] 
    (sort (flatten (keep-indexed (fn [index item] (let [offset (- (* (- number-of-bytes 1) 8) (* index 8))] (map #(+ % offset) (set-bits item)))) bytes)))))

(defn little-endian-all-set-bits [bytes]
  (sort (map #(- (* 8 (count bytes)) %) (all-set-bits bytes)))) 