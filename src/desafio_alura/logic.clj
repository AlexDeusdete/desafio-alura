(ns desafio-alura.logic
  (:require [java-time :as jtime]))

(defn total-das-compras
  [compras]
  (->> compras
       (map :valor)
       (reduce +)))

(defn total-das-categorias
  [[categoria compras]]
  {:categoria categoria
   :total     (total-das-compras compras)})

(defn total-das-compras-por-categoria
  [compras]
  (->> compras
       (group-by :categoria)
       (map total-das-categorias)))

(defn total-das-compras-no-cartao-e-resumo-por-categoria
  [[cartao compras]]
  {:cartao     (:numero cartao)
   :total      (total-das-compras compras)
   :categorias (total-das-compras-por-categoria compras)})

(defn resumo-por-cartao-e-categoria
  [compras]
  (->> compras
       (group-by :cartao)
       (map total-das-compras-no-cartao-e-resumo-por-categoria)))

;(defn todas-as-compras-de-uma-categoria
;  [compras categoria]
;  (filter #(= (:categoria %) categoria) compras))
;
;
;(defn todas-as-compras-de-um-estabelecimento
;  [compras estabelecimento]
;  (filter #(= (:estabelecimento %) estabelecimento) compras))
;
;(defn todas-as-compras-de-um-valor
;  [compras valor]
;  (filter #(= (:valor %) valor) compras))

(defn todas-as-compras-com-um-valor-de-atributo-especifico
  [compras atributo valor]
  (filter #(= (atributo %) valor) compras))

(defn compra-no-periodo?
  [compra periodo-inicio periodo-fim]
  (let [data-da-compra (:data compra)
        periodo-inicio-exclusive (jtime/minus periodo-inicio (jtime/days 1))
        periodo-fim-exclusive (jtime/plus periodo-fim (jtime/days 1))]
    (and (jtime/after? data-da-compra periodo-inicio-exclusive)
         (jtime/before? data-da-compra periodo-fim-exclusive))))

;(defn compra-no-periodo?
;  [compra periodo-inicio periodo-fim]
;  (let [data-da-compra (:data compra)]
;    (and (<= (jtime/time-between data-da-compra periodo-inicio :days) 0)
;         (>= (jtime/time-between data-da-compra periodo-fim :days) 0))))

(defn todas-as-compras-da-fatura
  [compras data-abertura data-fechamento]
  (filter #(compra-no-periodo? % data-abertura data-fechamento) compras))