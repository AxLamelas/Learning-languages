(ns genetic.core)


(def pop (atom []))
(def goal "Learning clojure!")
(def sizeEle (count goal))
(def goalVec (map int (seq (char-array goal))))
(def sizePop 10)

(defn genPop []
  (reset! pop [])
  (dotimes [n sizePop]  
    (swap! pop conj (repeatedly  sizeEle #(+ 32 (rand-int 94))))))

(defn dist [a b]
  (* (- a b) (- a b)))

(defn fitness [ele]
  (reduce + (map dist ele goalVec)))

(defn rank []
  (let [scores (atom [])]
    (dotimes [n sizePop]
      (swap! scores conj  [(fitness (get @pop n)) (get @pop n)]))
    (into (sorted-map) @scores)))

(defn selPerc []
  (let [ranking (rank) scores (map key ranking) values (vals ranking) max (reduce + scores)]
    (into (sorted-map) (map vector (map / scores (repeat (count scores) max)) values))))

(defn nextGen []
  
  (let [a (rand 1) b (rand 1) popPer (selPerc) per (map key popPer) perSumed (atom [])]
    (reset! pop [])
    (dotimes [n sizePop]
      (dotimes [i (sizePop)]
        (swap! perSumed conj (reduce + (subvec per 0 (+ i 1)))))
      (let [eleA (get values (- (first (filter neg? (map - (repeat sizePop a) @perSumed))) 1)) eleB (get values (- (first (filter neg? (map - (repeat sizePop b) @perSumed))) 1))]
        (swap! pop conj (str (subs eleA 0 (int (/ sizeEle 2))) (subs eleB (+ 1 (int (/ sizeEle 2)))))))
    )))



(genPop 10 2)
(println @pop)