(ns desafio-alura.desafio-semana1
  (:use clojure.pprint)
  (:require [desafio-alura.db :as da.db]
            [desafio-alura.logic :as da.logic]
            [java-time :as jtime]
            [datomic.api :as d]
            [desafio-alura.model :as model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(da.db/apaga-banco!)
(def conn (da.db/abre-conexao!))
(da.db/cria-schema! conn)

(defn convert-localdate-to-inst [param]
  (println "param")
  (println (class (second param))))

(let [cartao1 (model/novo-cartao :fisico "0000 0000 0000 0001" 222 (jtime/local-date 2030 01) 9000.00M)
      cartao2 (model/novo-cartao :virtual "0000 0000 0000 0002" 123 (jtime/local-date 2027 01) 1000.00M)
      cartao3 (model/novo-cartao :fisico "0000 0000 0000 0003" 333 (jtime/local-date 2025 01) 50.00M)
      cliente1 (model/novo-cliente (model/uuid) "Gabriela Lima" "000.000.000.00" "gabi.lima@teste.com.br" [cartao1 cartao2])
      cliente2 (model/novo-cliente (model/uuid) "Alex Reis" "111.111.111.11" "alex.reis@teste.com.br" [cartao3])
      cliente3 (model/novo-cliente (model/uuid) "Teste Teste" "444.555.444.33" "teste.teste@teste.com.br")
      compra1 (model/nova-compra (jtime/local-date 2021 06 20) 500.00M :super-mercado-extra :alimentacao (dissoc cliente1 :cliente/cartoes) (get-in cliente1 [:cliente/cartoes 0]))
      compra2 (model/nova-compra (jtime/local-date 2021 06 20) 900.00M :farma-sao-paulo :saude (dissoc cliente1 :cliente/cartoes) (get-in cliente1 [:cliente/cartoes 1]))
      compra3 (model/nova-compra (jtime/local-date 2021 06 01) 150000.00M :unip :educacao (dissoc cliente2 :cliente/cartoes) (get-in cliente2 [:cliente/cartoes 0]))
      compra4 (model/nova-compra (jtime/local-date 2021 07 03) 20.00M :super-mercado-pao-acucar :alimentacao (dissoc cliente1 :cliente/cartoes) (get-in cliente1 [:cliente/cartoes 0]))
      compra5 (model/nova-compra (jtime/local-date 2021 07 10) 55000.00M :farma-droga :saude (dissoc cliente1 :cliente/cartoes) (get-in cliente1 [:cliente/cartoes 0]))]
  (da.db/adiciona-altera-cliente! conn [cliente1 cliente2 cliente3])
  (da.db/adiciona-altera-compras! conn [compra1 compra2 compra3 compra4 compra5])
  )

(println ":::::::::::Imprimindo:::::::::::::::::::")

(println ":::::::::::Clientes:::::::::::::::::::")
(pprint (da.db/todos-os-clientes (d/db conn)))


(println "\n\n\n:::::::::::Compras:::::::::::::::::::")
(pprint (da.db/todas-as-compras (d/db conn)))

(println "\n\n\n:::::::::::Relatorios:::::::::::::::::::")
(println "\n:::::::::::Clientes com mais compras:::::::::::::::::::")
(pprint (da.db/clientes-com-maior-numero-de-compras (d/db conn)))

(println "\n:::::::::::Clientes sem Compras:::::::::::::::::::")
(pprint (da.db/clientes-que-nunca-realizaram-compras (d/db conn)))

(println "\n:::::::::::Cliente com a compra de maior valor:::::::::::::::::::")
(pprint (da.db/clientes-com-a-compra-de-maior-valor (d/db conn)))
