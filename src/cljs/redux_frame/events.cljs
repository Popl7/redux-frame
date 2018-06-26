(ns redux-frame.events
  (:require
   [re-frame.core :as re-frame]
   [redux-frame.db :as db]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
