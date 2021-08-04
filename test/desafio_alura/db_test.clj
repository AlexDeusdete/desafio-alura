(ns desafio-alura.db-test
  (:require [clojure.test :refer :all]
            [desafio-alura.db :refer :all]
            [desafio-alura.model :as da.model]
            [java-time :as jtime]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [schema.core :as s]
            [schema-generators.generators :as g]
            [schema-generators.complete :as c]))

(s/set-fn-validation! true)

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
(def uma-compra-qualquer {:id              1
                          :data            (jtime/local-date 2021 06 20)
                          :valor           500.00M
                          :estabelecimento :super-mercado-extra
                          :categoria       :alimentacao
                          :cliente         (dissoc cliente1 :cartoes)
                          :cartao          (get-in cliente1 [:cartoes 0])})


(deftest adiciona-compra-test

  (testing "Adiciona uma compra na lista vazia"
    (let [lista-compras-inicial []
          lista-compras-final (adiciona-compra lista-compras-inicial uma-compra-qualquer)]
      (is (> (count lista-compras-final) (count lista-compras-inicial))))))