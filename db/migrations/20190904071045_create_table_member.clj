(ns migrations.20190904071045-create-table-member
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :member
                (text :email :null false :unique true)
                (text :display-name)
                (text :password :null false)
                (timestamps)))
