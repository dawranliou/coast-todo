(ns member
  (:require [coast]
            [buddy.hashers :as hashers]))

(defn build
  [request]
  (coast/form-for
    ::create
    [:input {:type "text" :name "member/email"}]
    [:input {:type "password" :name "member/password"}]
    [:input {:type "submit" :name "Submit"}]))

(defn create
  [request]
  (let [[_ errors] (-> (:params request)
                       (select-keys [:member/email :member/password])
                       (coast/validate [[:email [:member/email]]
                                        [:required [:member/email :member/password]]])
                       (update :member/password hashers/derive)
                       (coast/rescue))]
    (if (some? errors)
      (build (merge errors request))
      (-> (coast/redirect-to :video/build)
          (assoc :session (select-keys (:params request) [:member/email]))))))
