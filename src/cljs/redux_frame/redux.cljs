(ns redux-frame.redux
  (:require [re-frame.core :as rf]
            [cljs.reader :refer [read-string]]))

(def ^:dynamic *redux-event-sent* false)

(defn set-redux-event-sent! [sent?]
  (set! *redux-event-sent* sent?))

(def ignored-events [])

(defn reducer [_ action]
  (or (.-reducerResult action)
      {}))

(def with-dev-tools
  (try
    (and
      (exists? js/window)
      (.devToolsExtension js/window))
    (catch :default _
      false)))

(defn setup []
  (when with-dev-tools
    (let [store (.devToolsExtension js/window reducer)]
      (set! js/window.store store)
      (.subscribe js/window.store
                  #(let [new-state (-> (.getState js/window.store)
                                       (js->clj :keywordize-keys true)
                                       (:cljs)
                                       (read-string))]
                     (rf/dispatch [:load-db new-state]))))))

(defn dispatch [action]
  (when with-dev-tools
    (when-let [store js/window.store]
      (.dispatch store (clj->js action)))))

(def redux-debug
  (re-frame.core/->interceptor
    :id    :bm-debug
    :after (fn [{:keys [coeffects effects] :as context}]
             (let [[name & args] (:event coeffects)
                   db          (or (:db effects)
                                   (:db coeffects))
                   db (assoc db :cljs (pr-str db))]
               (when (not-any? #(= % name) ignored-events)
                 (let [action {:type (str name)
                               :reducerResult db}
                       action (if (map? args)
                                (merge action args)
                                (assoc action :values args))]
                   (set-redux-event-sent! true)
                   (dispatch action)
                   (js/setTimeout (fn [] (set-redux-event-sent! false)) 100))))
             context)))

(rf/reg-event-db
  :load-db
  (fn [db [_ new-db]]
    (if (or *redux-event-sent* ;; not time-travelling, just updating external db
            (= new-db {}))     ;; prevent going back to before initialize-db
      db
      (do
        (.warn js/console "setting db to: " new-db)
        new-db))))
