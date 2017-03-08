(ns cljsplayground.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

(def todo-list (atom [{:id 1 :title "Buy groceries" :status :todo}
                      {:id 2 :title "Pay mobile bill" :status :todo}
                      {:id 3 :title "Take shower" :status :done}]))

(defn filter-completed [list]
  (filter #(= :todo (:status %)) list))


(defn clear-completed-btn []
  (let [clear-completed-fn (fn []
                             (swap! todo-list filter-completed))]
    [:button {:on-click clear-completed-fn} "Clear Completed"]))

(defn toggle-status [item]
  (if (= (:status item) :todo)
    (assoc item :status :done)
    (assoc item :status :todo)))

(defn toggle-status-of [item-id]
  (swap! todo-list (fn [list]
                     (map #(if (= item-id (:id %)) (toggle-status %) %) list))))

(defn show-list []
  [:ul
   (for [item @todo-list]
     [:li {:class (:status item) :on-click #(toggle-status-of (:id item))} (:title item)])])

(defn generate-new-id []
  (inc (apply max (map :id @todo-list))))

(defn add-item [title]
  (swap! todo-list (fn [list]
                     (conj list {:id (generate-new-id) :title title :status :todo}))))

(defn add-item-component []
  [:div
   [:input {:type :text :placeholder "Add new todo" :id :add-todo}]
   [:button {:on-click (fn []
                         (add-item (.-value (.getElementById js/document "add-todo")))
                         (set! (.-value (.getElementById js/document "add-todo")) ""))} "Add"]])

(defn current-page []
  [:div
   [:h1 {:style {:color :red :text-align :center}}  "TODO List"]
   [show-list]
   [clear-completed-btn]
   [add-item-component]
   ])


(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))


(defn greeting-component [name]
  [:h3 {:style {:color :red}} "Hello " name])

(defn nested-component []
  [:div
   [greeting-component "Bangalore Clojure Users!"]
   [:p "have fun!"]])
