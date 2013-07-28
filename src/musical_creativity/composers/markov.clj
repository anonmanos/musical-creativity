(ns musical-creativity.composers.markov
  (:require
   [musical-creativity.events :as events]))

(load-file "data/bach.clj")

(def default-events
  [{:pitch 60 :time 0}
   {:pitch 60 :time 0}
   {:pitch 62 :time 1000}
   {:pitch 64 :time 2000}
   {:pitch 65 :time 3000}
   {:pitch 67 :time 4000}
   {:pitch 65 :time 5000}
   {:pitch 69 :time 6000}
   {:pitch 71 :time 7000}
   {:pitch 72 :time 8000}
   {:pitch 60 :time 9000}])

(def defaults
  {:start [60]
   :events default-events
   :length 50
   :depth 1})

(defn pick-pitches-fn [stm depth]
  (fn [pitches]
    (let [last-chunk (take-last (inc depth) pitches)
          candidates (stm last-chunk)
          picked (rand-nth candidates)]
      (if picked
        (conj pitches picked)
        pitches))))

(defn- compose-pitches [start length depth stm]
  (nth (iterate (pick-pitches-fn stm depth) start) length))

(defn probabilities-for [stm chunk]
  (let [prefix (drop-last chunk)
        suffix (last chunk)
        stm-row (or (stm prefix) [])]
    (assoc stm prefix (conj stm-row suffix))))

(defn- state-transition-matrix-probabilities [pitches depth]
  (let [pitch-chunks (partition (inc depth) 1 pitches)]
    (reduce probabilities-for {} pitch-chunks)))

(defn- compose-markov [start length depth stm]
  (events/make (flatten (compose-pitches start length depth stm)) 0 350))

(defn compose
  ([] (compose (:events defaults) (:start defaults) (:length defaults) (:depth defaults)))
  ([events start length depth]
     (let [pitches (map :pitch events)
           stm (state-transition-matrix-probabilities pitches depth)]
       (compose-markov start length depth stm))))
