(ns sum-a-string.core-test
  (:require [expectations :refer :all]
            [sum-a-string.core :refer :all]))

;;An empty string should return 0
(expect 0 (sum-a-string ""))

;;A string with just one number should return the number
(expect 1 (sum-a-string "1"))

;;A string with multiple numbers should return their sum
(expect 3 (sum-a-string "1,2"))

;;A negative number should throw an Exception
(expect Exception (sum-a-string "asda-2"))
