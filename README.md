#Computer models of musical creativity

Experiments with models for computers generating music.

##Examples

```clojure
(require '[musical-creativity.musician :as musician] :reload)
(use '[musical-creativity.instruments] :reload)

;Cosine
(require '[musical-creativity.composers.cosine :as cosine] :reload)
(musician/play (cosine/compose) piano)

;Cellular automata
(require '[musical-creativity.composers.cellular-automata :as ca] :reload)
(musician/play (ca/compose) organ)
(musician/play (ca/compose ca/rule-22) sawish)

;Sonify data
(require '[musical-creativity.composers.sonify-data :as sonify-data] :reload)
(musician/play (sonify-data/compose) piano)

;Sonify text
(require '[musical-creativity.composers.sonify-words :as sonify-words] :reload)
(musician/play (sonify-words/compose "Strange Loop Strange Loop") piano)
(musician/play (sonify-words/compose "potato potato") ping)

;Network (Using adaptive resonance theory)
(require '[musical-creativity.composers.network :as network] :reload)
(musician/play (network/compose) piano)

;Markov chains
(require '[musical-creativity.composers.markov :as markov] :reload)
(musician/play (markov/compose) piano)
(musician/play (markov/compose) ping)

(musician/play (markov/compose data.bach/bach1 [57 60 69] 1 50) piano)

;with phrase mapping
(musician/play (markov/compose data.bach/bach1 [57] 16 (* 5 16)) piano)

;with naive voice extraction and phrase mapping
(require '[musical-creativity.analyzer :as analyzer] :reload)
; first extracted voice only and phrase mapping
(musician/play (markov/compose (first (-> data.bach/bach1
analyzer/group-by-time analyzer/extract-voices-min-distance)) [57] 16
(* 16 5)) piano)
; all voices added to model separately and phrase mapping
(musician/play (markov/compose (flatten (-> data.bach/bach1
analyzer/group-by-time analyzer/extract-voices-min-distance)) [57] 16
(* 16 5)) piano)

;multiple markov models
(let [lead-voice    1
      standard-prep #(-> %
                         analyzer/group-by-time
                         analyzer/extract-voices-min-distance)
      dur-prep      #(->> %
                    analyzer/group-by-time
                    analyzer/extract-durations
                    (map (fn [d] {:duration d}))
                    vector)]
    (markov/compose-multi-model
     data.bach/bach1
     50
     :pitch     (markov/transition-source standard-prep
                                          1 8 1 lead-voice)
     :voices    (markov/transition-source #(-> %
                                               analyzer/group-by-time
                                               vector)
                                          1 4 1 0)
     :harmonics (markov/harmonic-source standard-prep lead-voice [0 2 3])
     :duration  (markov/transition-source dur-prep
                                          1 4 4 :all)
     :velocity  (markov/constant-source 128)))

;Fuzzy
(require '[musical-creativity.composers.fuzzy :as fuzzy] :reload)
(musician/play (fuzzy/compose) piano)

;Improvise
;(very experimental)
(require '[musical-creativity.composers.improvise :as improvise] :reload)
(musician/play (improvise/compose) piano)

(require '[musical-creativity.band :as band] :reload)
(band/play (improvise/compose))

;Recombinance (Based on Bach Choral no. 140)
(require '[musical-creativity.composers.recombinance :as recombinance] :reload)
(require '[data.bach :as bach] :reload)
(recombinance/create-database-from bach/chorale-140-data)
(musician/play (recombinance/compose) organ)
(musician/play (recombinance/compose) piano)
(musician/play (recombinance/compose) slow-piano)

(musician/play (recombinance/compose-original) piano)

;Gradus (with each run Gradus learns how to better compose)
(require '[musical-creativity.composers.gradus :as gradus] :reload)

(musician/play (gradus/compose) piano)

(musician/play (gradus/compose-canon) piano)

(musician/play (gradus/compose-canon data.gradus/cantus-firmus-1) piano)
(musician/play (gradus/compose-canon data.gradus/cantus-firmus-2) piano)
(musician/play (gradus/compose-canon data.gradus/cantus-firmus-7) piano)

(musician/play (gradus/compose-contemporary) piano)
```

### Association Network (Apprentice)

```clojure
(require '[musical-creativity.composers.apprentice :as apprentice] :reload)
(require '[musical-creativity.composers.apprentice.player :refer :all] :reload)

(apprentice/apprentice play-all)
```

An example musical conversion:

```
user> ceg!
user> gbd?
alice> ceg!
user> ceg fac gbd?
alice> fac ceg ceg!
user> ceg fac gbd ceg!
alice> gbd?
user> ceg!
alice> gbd?
user> gbd?
alice> ceg!
```

Weightings:

![Weightings](http://s7.postimg.org/r1igtc3wr/png8043032250889381797.png)


A non musical dialog example:

```
user> hello!
Alice> your what your is name?
user> my name is joseph!
Alice> name?
user> your name is computer!
Alice> name?
user> what is my name?
Alice> joseph!
user> what is your name?
Alice> computer!
user> no*
user> what is your name?
Alice> joseph!
```

### Demos

```clojure
;;b11606b
(musical-creativity.musician/play (map musical-creativity.events/midi-to-event data.bach.chorale-140/b11606b) piano)

(require '[musical-creativity.composers.recombinance :as recombinance] :reload)
(require '[data.bach :as bach] :reload)
(recombinance/create-database-from bach/chorale-140-data)

(musical-creativity.musician/play (map musical-creativity.events/midi-to-event ('b11606b-22 @beats-store)))
```

##Book

Based on exercises from:

![Computer Models of Musical Creativity](http://mitpress.mit.edu/covers/9780262033381.jpg)

Computer Models of Musical Creativity http://mitpress.mit.edu/books/computer-models-musical-creativity
