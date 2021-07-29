(ns desafio-alura.logic)

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