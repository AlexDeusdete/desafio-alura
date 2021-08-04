(ns desafio-alura.db
  (:require [java-time :as jtime]
            [desafio-alura.model :as model]
            [schema.core :as s]))

(s/defn adiciona-cliente
  [clientes :- [model/Cliente] cliente :- model/Cliente ]
  (conj clientes cliente))

(s/defn adiciona-cartao
  [clientes :- [model/Cliente] cliente :- model/Cliente cartao :- model/Cartao]
  )

(s/defn adiciona-compra
  "adiciona uma nova compra"
  [compras :- [model/Compra] compra :- model/Compra]
  (conj compras compra)
  )