(ns com.oakmac.iso8583.util.string)

(defn left-pad
  [s l ch]
  (let [s-length (count s)]
    (if (< s-length l)
      (let [infinite-chars (repeat ch)
            pad-amount (- l s-length)
            prefix (->> (take pad-amount infinite-chars)
                     (apply str))]
        (str prefix s))
      s)))
