(ns site.home
  (:require [coast]))


(defn index [request]
  (let [rows (coast/q '[:select *
                        :from todo
                        :order id desc])]
    [:div.pa3.measure.center
     [:h1.ttu.tracked "All todos"]
     (if (seq rows)
       [:ul.list.pl0
        (for [{:todo/keys [name finished]} rows]
          [:li.lh-copy.pv3.bb.b--black-30
           {:class (when (= 1 finished) "strike")}
           name])]
       [:p "Nothing to do!"])]))
