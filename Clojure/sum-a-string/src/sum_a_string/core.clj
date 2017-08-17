(ns sum-a-string.core)

(defn sum-a-string [s]
  (let [nums (map #(Integer/parseInt %) (re-seq #"[\d]+|[-\d]+" s)) ]
   (when-let [negatives  (not-empty (filter neg? nums))]
     (throw (Exception. (str "Negatives not allowed: " negatives))))
   (reduce + nums)))







