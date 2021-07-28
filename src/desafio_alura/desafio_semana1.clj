(ns desafio-alura.desafio-semana1
  (:require [desafio-alura.db :as da.db]))

(def compras (da.db/todas-as-compras))
(def clientes (da.db/todos-os-clientes))

(println compras)
(println clientes)

