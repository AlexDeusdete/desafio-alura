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

(println todas-as-compras)
(println (filter #(= (:categoria %) :saude) todas-as-compras))



;(println (todas-as-compras-de-um-estabelecimento todas-as-compras :farma-droga))
;(println (todas-as-compras-de-um-valor todas-as-compras 500.00))

(println (da.logic/todas-as-compras-com-um-valor-de-atributo-especifico
           todas-as-compras
           :estabelecimento
           :farma-droga))
(println (da.logic/todas-as-compras-com-um-valor-de-atributo-especifico
           todas-as-compras
           :valor
           500.00))