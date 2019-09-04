(ns member
  (:require [coast]
            [buddy.hashers :as hashers]
            [components :refer [container input submit]]))

(defn build
  [request]
  (container
    {:mw 6}
    [:h1 "Sign up"]
    (coast/form-for
      ::create
      (input {:type "email" :name "member/email"})
      (input {:type "password" :name "member/password"})
      (submit "Submit"))))

(defn create
  [request]
  (let [[member errors] (-> (:params request)
                            (select-keys [:member/email :member/password])
                            (coast/validate [[:email [:member/email]]
                                             [:required [:member/email :member/password]]])
                            (update :member/password hashers/derive)
                            (coast/insert)
                            (coast/rescue))]
    (if (some? errors)
      (build (merge errors request))
      (-> (coast/redirect-to ::dashboard)
          (assoc :session (select-keys (:params request) [:member/email]))))))

(defn dashboard
  [request]
  (container
    {:mw 6}
    [:h1 "Dashboard"]
    (coast/form-for
      :session/delete
      (submit "Sign out"))))

(comment
  ;; manually insert record
  (-> {:member/email    "aaa"
       :member/password "bbb"}
      (update :member/password hashers/derive)
      (coast/insert)))
