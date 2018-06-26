(ns redux-frame.events
  (:require
    [redux-frame.helpers :refer [reg-event-db]]
    [redux-frame.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-db
  ::add-number
  (fn [db [_ new-number]]
    (let [old-numbers (:numbers db)
          new-numbers (set (conj old-numbers new-number))]
      (assoc db :numbers new-numbers))))
