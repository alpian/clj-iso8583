(ns com.oakmac.iso8583.util.string-test
  (:require
    [clojure.test :refer [deftest is]]
    [com.oakmac.iso8583.util.string :as util.str]))

(deftest left-pad-test
  (is (= (util.str/left-pad "" 0 "") ""))
  (is (= (util.str/left-pad " " 2 "1") "1 "))
  (is (= (util.str/left-pad "17" 3 "0") "017"))
  (is (= (util.str/left-pad "foo" 2 "*") "foo"))
  (is (= (util.str/left-pad "foo" 3 "*") "foo"))
  (is (= (util.str/left-pad "foo" 4 "*") "*foo"))
  (is (= (util.str/left-pad "foo" 5 "*") "**foo"))
  (is (= (util.str/left-pad "foo" 6 "*") "***foo"))
  (is (= (util.str/left-pad "foo" 7 "*") "****foo")))
