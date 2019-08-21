(ns todo
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))


(defn index [request]
  (let [rows (coast/q '[:select *
                        :from todo
                        :order id
                        :limit 10])]
    (container {:mw 8}
     (when (not (empty? rows))
      (link-to (coast/url-for ::build) "New todo"))

     (when (empty? rows)
      (tc
        (link-to (coast/url-for ::build) "New todo")))

     (when (not (empty? rows))
       (table
        (thead
          (tr
            (th "id")
            (th "updated-at")
            (th "name")
            (th "created-at")
            (th "finished")))
        (tbody
          (for [row rows]
            (tr
              (td (:todo/id row))
              (td (:todo/updated-at row))
              (td (:todo/name row))
              (td (:todo/created-at row))
              (td (:todo/finished row))
              (td
                (link-to (coast/url-for ::view row) "View"))
              (td
                (link-to (coast/url-for ::edit row) "Edit"))
              (td
                (button-to (coast/action-for ::delete row) {:data-confirm "Are you sure?"} "Delete"))))))))))


(defn view [request]
  (let [id (-> request :params :todo-id)
        todo (coast/fetch :todo id)]
    (container {:mw 8}
      (dl
        (dt "name")
        (dd (:todo/name todo))

        (dt "finished")
        (dd (:todo/finished todo)))
      (mr2
        (link-to (coast/url-for ::index) "List"))
      (mr2
        (link-to (coast/url-for ::edit {::id id}) "Edit"))
      (mr2
        (button-to (coast/action-for ::delete {::id id}) {:data-confirm "Are you sure?"} "Delete")))))


(defn errors [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (dt (str k))
       (dd v)])]])


(defn build [request]
  (container {:mw 6}
    (when (some? (:errors request))
     (errors (:errors request)))

    (coast/form-for ::create
      (label {:for "todo/name"} "name")
      (input {:type "text" :name "todo/name" :value (-> request :params :todo/name)})

      (label {:for "todo/finished"} "finished")
      (input {:type "text" :name "todo/finished" :value (-> request :params :todo/finished)})

      (link-to (coast/url-for ::index) "Cancel")
      (submit "New todo"))))


(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:todo/name :todo/finished]]])
                       (select-keys [:todo/name :todo/finished])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (build (merge request errors)))))


(defn edit [request]
  (let [todo (coast/fetch :todo (-> request :params :todo-id))]
    (container {:mw 6}
      (when (some? (:errors request))
        (errors (:errors request)))

      (coast/form-for ::change todo
        (label {:for "todo/name"} "name")
        (input {:type "text" :name "todo/name" :value (:todo/name todo)})

        (label {:for "todo/finished"} "finished")
        (input {:type "text" :name "todo/finished" :value (:todo/finished todo)})

        (link-to (coast/url-for ::index) "Cancel")
        (submit "Update todo")))))


(defn change [request]
  (let [todo (coast/fetch :todo (-> request :params :todo-id))
        [_ errors] (-> (select-keys todo [:todo/id])
                       (merge (:params request))
                       (coast/validate [[:required [:todo/id :todo/name :todo/finished]]])
                       (select-keys [:todo/id :todo/name :todo/finished])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (edit (merge request errors)))))


(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :todo (-> request :params :todo-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))
