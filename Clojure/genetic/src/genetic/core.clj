(ns genetic.core)


(def goal "Learning clojure!")
(def sizePop 200)
(def mRate 0.1)


(def sizeEle (count goal))
(def goalVec (map int (seq (char-array goal))))

(defn genEle [sizeEle s]
    (if (= sizeEle (count s))
      s
      (recur sizeEle (conj s  (+ 32 (rand-int 94))))))

(defn genPop [sizePop sizeEle]
  (map #(genEle sizeEle %) (repeat sizePop [])))
    
(defn dist [a b]
  (* (- a b) (- a b)))

(defn fitness [ele goalVec]
  (reduce + (map dist goalVec ele)))

(defn rank [pop goalVec]
  (into (sorted-map) (map vector (map fitness pop (repeat (count pop) goalVec)) pop)))

(defn select [ranking]
  (nth (vals ranking) (apply min (map rand-int (repeat 5  (count ranking))))))
    

(defn mutate [c mutate?]
  (if mutate?
    (+ (rand-int 94) 32)
    c))

(defn mutateVec [v mutVec]
  (map mutate v mutVec))

(defn genMutVec [mRate sizeEle]
  (map (fn [mRate] (>= mRate (rand 1))) (repeat sizeEle mRate)))

(defn repro [v1 v2 mRate]
  (let [splitPoint (int (/ (count v1) 2)) v (concat (subvec v1 0 splitPoint) (subvec v2 splitPoint))]
    (vec (map mutate v (genMutVec mRate (count v))))))
    

  
(defn nextGen [pop goalVec mRate]
  (let [ranking (rank pop goalVec) sizePop (count pop) p1 (vec (map select (repeat sizePop ranking))) p2 (vec (map select (repeat sizePop ranking)))]
    (map repro p1 p2 (repeat sizePop mRate)))))

(defn inside? [vec ele]
  (>= (count (filter (fn [x] (= x ele)) vec)) 1))

(defn evolve [pop goalVec mRate generation]
  (let [ranking (rank pop goalVec)]
    (println "Best string: " (nth (vals ranking) (apply min (keys ranking)))))
  (println "Generation: " generation "\n")  
  (if (inside? pop goalVec)
    (println "Finished evolving: " (apply str(map char (nth pop (.indexOf pop goalVec)))))
  (recur (nextGen pop goalVec mRate) goalVec mRate (inc generation))))




(evolve (genPop sizePop sizeEle) goalVec mRate 0)