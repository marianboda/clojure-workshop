(ns workshop.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [workshop.todos :as todos])
    (:import goog.History))

;; -------------------------
;; Views

(defonce app-db
  (atom
    {:clicks 0
    :todos ["nakup"]  }))

(defn home-page []
  [:div [:h2 "Welcome to workshop"]
   [:div [:a {:href "#/about"} "go to about page"]]
   [:section#bar.foo
    [:h3 "Clicks: " (get @app-db :clicks)]
    [:p
      [:a {:on-click #(swap! app-db update :clicks + 1000)} "Do Something"]
      [:a {:on-click #(swap! app-db assoc :clicks 0)} "RESET"]
    ]
    ]
   ])

(defn about-page []
  [:div [:h2 "About workshop - naozaj???"]
   [:div [:a {:href "#/"} "go to the home page"]]
   [:section.todo-list
    [:ul (map todos/todo-item (get @app-db :todos))]
   ]
   [:p [todos/todo-editor app-db]]
   ])
(defn sranda-page []
 [:div [:h2 "Nejaka sranda"]
  [:div [:a {:href "#/"} "go to the home page"]]
  [:hr {}]
  ])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/sranda" []
  (session/put! :current-page #'sranda-page))


;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
