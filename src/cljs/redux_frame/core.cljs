(ns redux-frame.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [redux-frame.events :as events]
   [redux-frame.views :as views]
   [redux-frame.config :as config]
   [redux-frame.redux :as redux]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")
    (redux/setup)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (dev-setup)
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
