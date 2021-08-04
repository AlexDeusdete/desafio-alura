(ns desafio-alura.model
  (:require [schema.core :as s]
            [java-time :as jtime]))

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def Cartao
  {:id       PosInt
   :tipo     s/Keyword
   :numero   s/Str
   :cvv      PosInt
   :validade java.time.LocalDate
   :limite   BigDecimal})

(def Cliente
  {:id                       PosInt
   :nome                     s/Str
   :cpf                      s/Str
   :e-mail                   s/Str
   (s/optional-key :cartoes) [Cartao]})

(def Compra
  {:id              PosInt
   :data            java.time.LocalDate
   :valor           BigDecimal
   :estabelecimento s/Keyword
   :categoria       s/Keyword
   :cliente         Cliente
   :cartao          Cartao})