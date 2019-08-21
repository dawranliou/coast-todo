(ns migrations.20190820103921-create-table-todo
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :todo
    (text :name)
    (bool :finished)
    (timestamps)))