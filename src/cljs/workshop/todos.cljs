(ns workshop.todos
  (:require [reagent.core :as reagent :refer [atom]]
))
(defn todo-item
  [t]
  [:li t])
(defn todo-editor
  [app-db]
  (let [t (atom nil)]
    (fn []
      [:div.editor
        [:input {:type :text :value @t
          :on-change (fn [e]  (reset! t (.. e -target -value )))}]
        [:button {:on-click
          (fn []
            (swap! app-db update :todos conj @t)
            (reset! t nil)
          )} "ok"]
      ]
    )
  )
  )
