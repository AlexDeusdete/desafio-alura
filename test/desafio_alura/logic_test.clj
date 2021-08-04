(ns desafio-alura.logic-test
  (:require [clojure.test :refer :all]
            [desafio-alura.logic :refer :all]
            [desafio-alura.db :as da.db]
            [java-time :as jtime]
            [schema.core :as s]))

(s/set-fn-validation! true)

(def todas-as-compras [])
(def todos-os-clientes [])
(def cliente1 {:id      999
               :nome    "Gabriela Lima"
               :cpf     "000.000.000.00"
               :e-mail  "gabi.lima@teste.com.br"
               :cartoes [{:id       999111
                          :tipo     :virtual
                          :numero   "0000 0000 0000 0001"
                          :cvv      111
                          :validade (jtime/local-date 2025 07)
                          :limite   2000.00M}
                         {:id       999222
                          :tipo     :fisico
                          :numero   "0000 0000 0000 0002"
                          :cvv      222
                          :validade (jtime/local-date 2030 01)
                          :limite   9000.00M}]})
(def cliente2 {:id      888
               :nome    "Alex Reis"
               :cpf     "111.111.111-11"
               :e-mail  "alex.reis@teste.com.br"
               :cartoes [{:id       888111
                          :tipo     :fisico
                          :numero   "0000 0000 0000 8881"
                          :cvv      881
                          :validade (jtime/local-date 2025 12)
                          :limite   2000.00M}]})
(def todos-os-clientes (da.db/adiciona-cliente todos-os-clientes cliente1))
(def todos-os-clientes (da.db/adiciona-cliente todos-os-clientes cliente2))

(def compra1 {:id              1
              :data            (jtime/local-date 2021 06 20)
              :valor           500.00M
              :estabelecimento :super-mercado-extra
              :categoria       :alimentacao
              :cliente         (dissoc cliente1 :cartoes)
              :cartao          (get-in cliente1 [:cartoes 0])})
(def compra2 {:id              2
              :data            (jtime/local-date 2021 06 20)
              :valor           900.00M
              :estabelecimento :farma-sao-paulo
              :categoria       :saude
              :cliente         (dissoc cliente1 :cartoes)
              :cartao          (get-in cliente1 [:cartoes 1])})
(def compra3 {:id              3
              :data            (jtime/local-date 2021 06 01)
              :valor           1500.00M
              :estabelecimento :unip
              :categoria       :educacao
              :cliente         (dissoc cliente2 :cartoes)
              :cartao          (get-in cliente2 [:cartoes 0])})
(def compra4 {:id              4
              :data            (jtime/local-date 2021 07 03)
              :valor           750.00M
              :estabelecimento :super-mercado-pao-acucar
              :categoria       :alimentacao
              :cliente         (dissoc cliente1 :cartoes)
              :cartao          (get-in cliente1 [:cartoes 0])})
(def compra5 {:id              5
              :data            (jtime/local-date 2021 07 10)
              :valor           150.00M
              :estabelecimento :farma-droga
              :categoria       :saude
              :cliente         (dissoc cliente1 :cartoes)
              :cartao          (get-in cliente1 [:cartoes 0])})
(def todas-as-compras (da.db/adiciona-compra todas-as-compras compra1))
(def todas-as-compras (da.db/adiciona-compra todas-as-compras compra2))
(def todas-as-compras (da.db/adiciona-compra todas-as-compras compra3))
(def todas-as-compras (da.db/adiciona-compra todas-as-compras compra4))
(def todas-as-compras (da.db/adiciona-compra todas-as-compras compra5))

(deftest todas-as-compras-com-um-valor-de-atributo-especifico-test

  (testing "busca lista de compra por um atributo existente"
    (is (= (count (todas-as-compras-com-um-valor-de-atributo-especifico todas-as-compras :categoria :saude)) 2)))

  (testing "busca lista de compra por um atributo inexistente"
    (is (= (count (todas-as-compras-com-um-valor-de-atributo-especifico todas-as-compras :nao-existe :saude)) 0))))

(deftest total-das-compras-por-categoria-test

  (testing "gera relatorio de uma lista de compras preenchida"
    (is (= (count (total-das-compras-por-categoria todas-as-compras)) 3)))

  (testing "gera relatorio de uma lista de compras vazia"
    (is (= (count (total-das-compras-por-categoria [])) 0)))

  (testing "gera relatorio de uma lista nula"
    (is (= (count (total-das-compras-por-categoria nil)) 0))))
