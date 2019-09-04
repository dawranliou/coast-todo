(ns routes
  (:require [coast]
            [components]
            [middleware]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout components/layout
        [:get "/" :site.home/index]
        #_[:get "/sign-up" :member/build]
        #_[:post "/members" :member/create]
        [:get "/sign-in" :session/build]
        [:post "/sessions" :session/create]

        (coast/with
          middleware/auth
          [:get "/dashboard" :member/dashboard]
          [:delete "/sessions" :session/delete]
          [:resource :todo])))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
