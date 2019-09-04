(ns session
  (:require [coast]
            [buddy.hashers :as hashers]
            [components :refer [container input submit]]))

(defn build
  [request]
  (container
    {:mw 6}
    [:h1 "Sign in"]
    (when (some? (:error/message request))
      [:div (:error/message request)])
    (coast/form-for
      ::create
      (input {:type "email" :name "member/email"})
      (input {:type "password" :name "member/password"})
      (submit "Submit"))))

(defn create
  [request]
  (let [email           (get-in request [:params :member/email])
        member          (coast/find-by :member {:email email})
        [valid? errors] (-> (:params request)
                            (select-keys [:member/email :member/password])
                            (coast/validate [[:email [:member/email]]
                                             [:required [:member/email :member/password]]])
                            (get :member/password)
                            (hashers/check (:member/password member))
                            (coast/rescue))]
    (if (or (some? errors)
            (false? valid?))
      (build (merge errors request {:error/message "Invalid email or password"}))
      (-> (coast/redirect-to :member/dashboard)
          (assoc :session {:member/email email})))))

(defn delete
  [request]
  (-> (coast/redirect-to ::build)
      (assoc :session nil)))
