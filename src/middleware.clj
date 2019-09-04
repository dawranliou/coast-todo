(ns middleware
  (:require [coast.responses]))

(defn auth [handler]
  (fn [request]
    (if (get-in request [:session :member/email])
      (handler request)
      (coast.responses/forbidden "HAL9000 says: I'm sorry Dave, I can't let you do that"))))
