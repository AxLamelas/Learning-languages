(ns exp.core
  (:gen-class))

(defn -main

  (defn fibo [n] 
    (if (or (= n 1) (= n 0)) 
      1
      (+ (fibo (dec n)) (fibo (- n 2)))))

  (def fibo-mem
    (memoize (fn [n] 
    (if (or (= n 1) (= n 0)) 
      1
      (+ (fibo (dec n)) (fibo (- n 2)))))))

  (time (fibo-mem 50))
  
  
  )


