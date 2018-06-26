(ns redux-frame.helpers
  (:require [re-frame.core :as rf]
            [redux-frame.redux :refer [redux-debug]]))

;; Custom Interceptors
;;
(def ignored-events [])

(def bm-debug
  (re-frame.core/->interceptor
    :id      :bm-debug
    :before  (fn [context]
               (let [[name args] (get-in context [:coeffects :event])]
                 (when (not-any? #(= % name) ignored-events)
                   (.log js/console "Event" name args)))
               context)))

(def standard-interceptors-db
  [bm-debug
   redux-debug])

(def standard-interceptors-fx
  [bm-debug
   redux-debug])


;; Custom registrations
;;
(defn reg-event-db
  ([id handler-fn]
   (rf/reg-event-db id
                    standard-interceptors-db
                    handler-fn))
  ([id interceptors handler-fn]
   (rf/reg-event-db id
                    [standard-interceptors-db interceptors]
                    handler-fn)))

(defn reg-event-fx
  ([id handler-fn]
   (rf/reg-event-fx id
                    standard-interceptors-fx
                    handler-fn))
  ([id interceptors handler-fn]
   (rf/reg-event-fx id
                    [standard-interceptors-fx interceptors]
                    handler-fn)))
