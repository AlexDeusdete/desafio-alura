(ns desafio-alura.desafio-semana1
  (:require [desafio-alura.db :as da.db]
            [desafio-alura.logic :as da.logic]))

(def todas-as-compras (da.db/todas-as-compras))
(def todos-os-clientes (da.db/todos-os-clientes))

(println todas-as-compras)
(println todos-os-clientes)

(println (group-by :cartao todas-as-compras))

(println (da.logic/resumo-por-cartao-e-categoria todas-as-compras))
(println (da.logic/total-das-compras-por-categoria todas-as-compras))