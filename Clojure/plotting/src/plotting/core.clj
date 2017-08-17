(ns plotting.core
  (:require [quil.core :as q]))

; define f
(defn f [t]
  [t
   (* 100 (q/cos (/ t (/ (q/frame-count) 60))))])

(defn draw-plot [f from to step]
  (doseq [two-points (->> (range from to step)
                          (map f)
                          (partition 2 1))]
    ; we could use 'point' function to draw a point
    ; but let's rather draw a line which connects 2 points of the plot
    (println two-points)
    (apply q/line two-points)))

(defn draw []
  (q/background 255)
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
   (draw-plot f 0 100 0.01)))

(q/defsketch dancer
  :size [300 300]
  :draw draw)