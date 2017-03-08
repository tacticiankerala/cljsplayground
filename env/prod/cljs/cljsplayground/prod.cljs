(ns cljsplayground.prod
  (:require [cljsplayground.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
