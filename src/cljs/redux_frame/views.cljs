(ns redux-frame.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [redux-frame.subs :as subs]
    [redux-frame.events :as events]))


(defn main-panel []
      (let [name (re-frame/subscribe [::subs/name])
            numbers (re-frame/subscribe [::subs/numbers])
            new-number (reagent/atom "42")]
           (fn []
               [:div
                [:h1 "Hello from " @name]
                [:ul
                 (for [number @numbers]
                      ^{:key number}
                      [:li number])]
                [:div
                 [:label]
                 [:input {:type :number
                          :value @new-number
                          :on-change #(reset! new-number (-> % .-target .-value))}]
                 [:button {:on-click #(do (re-frame/dispatch [::events/add-number @new-number])
                                          (reset! new-number ""))}
                  "add"]]])))
